package com.josephhowerton.apolisshopping

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.app.Endpoint
import com.josephhowerton.apolisshopping.database.DBHelper
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.category.CategoryResponse
import com.josephhowerton.apolisshopping.model.orders.*
import com.josephhowerton.apolisshopping.model.product.*
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubCategoryResponse
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.model.user.User
import org.json.JSONArray
import org.json.JSONObject

class Repository constructor(application: Application){
    private val sharedPreferences = application.getSharedPreferences(Config.SHARED_PREFERENCE_NAME, AppCompatActivity.MODE_PRIVATE)
    private val requestQueue:RequestQueue = Volley.newRequestQueue(application)
    private val gson:Gson = Gson()
    private val db: DBHelper = DBHelper(application)

    fun init() : LiveData<Boolean> {
        return fetchCategory()
    }

    fun isUserSignedIn() : Boolean {
        return sharedPreferences.getString(Config.CURRENT_USER, null) != null
    }

    private fun storeCurrentUser(id: String){
        val editor = sharedPreferences.edit()
        editor.putString(Config.CURRENT_USER, id)
        editor.apply()
    }

    private fun getCurrentUserId() : String? {
        return sharedPreferences.getString(Config.CURRENT_USER, null)
    }

    fun signUserOut(){
        val editor = sharedPreferences.edit()
        editor.putString(Config.CURRENT_USER, null)
        editor.apply()
    }

    fun signIn(email: String, password: String, listener: NetworkErrorListener) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()

        val jsonObject = JSONObject()

        jsonObject.put(DBHelper.EMAIL, email)
        jsonObject.put(DBHelper.PASSWORD, password)

        val request = JsonObjectRequest(
                Request.Method.POST,
                Config.getSignInUrl(),
                jsonObject,
                {
                    val data = it.getJSONObject("user")
                    val userName = data.getString(User.FIRST_NAME)
                    val userEmail = data.getString(User.EMAIL)
                    val userPassword = data.getString(User.PASSWORD)
                    val userPhone = data.getString(User.MOBILE)
                    val userV = data.getInt(User.V)
                    val userId = data.getString(User.ID)
                    val userCreatedAt = data.getString(User.CREATED_AT)
                    addUser(User(userName, userId, userEmail, userPassword, userPhone, userCreatedAt, userV))
                    storeCurrentUser(userId)
                    mutableLiveData.value = true
                },
                {
                    it.printStackTrace()
                    if(it.message != null){
                        listener.onNetworkError(it.message!!)
                    }else{
                        listener.onNetworkError("Error Signing In")
                    }
                    mutableLiveData.value = false
                }
        )
        requestQueue.add(request)

        return mutableLiveData

    }

    fun signUp(name: String, email: String, password: String, phone: String, listener: NetworkErrorListener) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()

        val jsonObject = JSONObject()

        jsonObject.put(DBHelper.FIRST_NAME, name)
        jsonObject.put(DBHelper.EMAIL, email)
        jsonObject.put(DBHelper.PASSWORD, password)
        jsonObject.put(DBHelper.MOBILE, phone)

        val request = JsonObjectRequest(
            Request.Method.POST,
            Config.getSignUpUrl(),
                jsonObject,
            {
                val data = it.getJSONObject("data");
                val userName = data.getString(DBHelper.FIRST_NAME)
                val userEmail = data.getString(DBHelper.EMAIL)
                val userPassword = data.getString(DBHelper.PASSWORD)
                val userPhone = data.getString(DBHelper.MOBILE)
                val userV = data.getInt(DBHelper.V)
                val userId = data.getString(DBHelper.ID)
                val userCreatedAt = data.getString(DBHelper.CREATED_AT)
                addUser(User(userName, userId, userEmail, userPassword, userPhone, userCreatedAt, userV))
                storeCurrentUser(userId)
                mutableLiveData.value = true
            },
            {
                it.printStackTrace()
                if(it.message != null){
                    listener.onNetworkError(it.message!!)
                }else{
                    listener.onNetworkError("Error Signing Up")
                }
                mutableLiveData.value = false
            }
        )
        requestQueue.add(request)

        return mutableLiveData
    }

    private fun fetchCategory() : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        if(isCategoryTableFresh()){
            mutableLiveData.value = true
        }else{
            val request = StringRequest(
                    Request.Method.GET,
                    Config.getBaseUrlWithEndpoint(Endpoint.CATEGORY),
                    {
                        val response = gson.fromJson(it, CategoryResponse::class.java)
                        for(category in response.data){
                            addCategory(category)
                        }
                        mutableLiveData.value = true
                    },
                    {
                        it.printStackTrace()
                        mutableLiveData.value = false
                    }
            )
            requestQueue.add(request)
        }

        return mutableLiveData
    }

    fun fetchSubCategory(id: Int) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        if(isSubcategoryTableFresh(id)){
            mutableLiveData.value = true
        }
        else{
            val request = StringRequest(
                    Request.Method.GET,
                    Config.getBaseUrlWithEndpoint(Endpoint.SUB_CATEGORY, id),
                    {
                        val response = gson.fromJson(it, SubCategoryResponse::class.java)
                        for(category in response.data){
                            addSubcategory(category)
                            fetchProducts(category.subId)
                        }
                        mutableLiveData.value = true
                    },
                    {
                        it.printStackTrace()
                        mutableLiveData.value = false
                    }
            )
            requestQueue.add(request)
        }
        return mutableLiveData
    }

    fun fetchProducts(id: Int) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        if(isProductTableFresh(id)){
            mutableLiveData.value = true
        }
        else{
            val request = StringRequest(
                    Request.Method.GET,
                    Config.getBaseUrlWithProductEndpoint(Endpoint.PRODUCTS, id),
                    {
                        val response = gson.fromJson(it, ProductResponse::class.java)
                        for(category in response.data){
                            addProduct(category)
                        }
                        mutableLiveData.value = true
                    },
                    {
                        it.printStackTrace()
                        mutableLiveData.value = false
                    }
            )
            requestQueue.add(request)
        }
        return mutableLiveData
    }

    fun fetchProductsDetails(id: String) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        val request = StringRequest(
            Request.Method.GET,
            Config.getBaseUrlWithProductDetailsEndpoint(Endpoint.PRODUCTS, id),
            {
                val response = gson.fromJson(it, ItemResponse::class.java)
                updateProduct(response.data)
                mutableLiveData.value = true
            },
            {
                it.printStackTrace()
                mutableLiveData.value = false
            }
        )
        requestQueue.add(request)
        return mutableLiveData
    }

    fun postAddress(name: String, phone: String, pincode: Int, city: String, country: String, streetName: String, houseNo: String, type: String,
                    listener: NetworkErrorListener) : LiveData<Boolean> {

        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        val jsonObject = JSONObject()

        jsonObject.put(Address.PIN_CODE, pincode)
        jsonObject.put(Address.CITY, city)
        jsonObject.put(Address.STREET_NAME, streetName)
        jsonObject.put(Address.HOUSE_NUMBER, houseNo)
        jsonObject.put(Address.TYPE, type)
        jsonObject.put(Address.USER_ID, getCurrentUserId()!!)

        val request = JsonObjectRequest(
            Request.Method.POST,
            Config.getAddressUrl(),
            jsonObject,
            {
                val data = it.getJSONObject(Config.DATA)
                val pinCode = data.getInt(Address.PIN_CODE)
                val responseCity = data.getString(Address.CITY)
                val responseStreetName = data.getString(Address.STREET_NAME)
                val houseNumber = data.getString(Address.HOUSE_NUMBER)
                val responseType = data.getString(Address.TYPE)
                val userId = data.getString(Config.USER_ID)
                val id = data.getString(Address.ID)
                val v = data.getInt(Address.V)
                mutableLiveData.value = addAddress(AddressWithNameAndPhone(id, name, phone,
                        houseNumber, responseStreetName, responseCity, country, responseType,
                        userId, pinCode, v))
            },
            {
                it.printStackTrace()
                if(it.message != null){
                    listener.onNetworkError(it.message!!)
                }else{
                    listener.onNetworkError("We could not save your address!")
                }
                mutableLiveData.value = false
            }
        )
        requestQueue.add(request)

        return mutableLiveData
    }

    fun updateAddress(addressId: String, name: String, phone: String, pincode: Int, city: String, country: String, streetName: String, houseNo: String, type: String,
                    listener: NetworkErrorListener) : LiveData<Boolean> {

        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        val jsonObject = JSONObject()

        jsonObject.put(Address.PIN_CODE, pincode)
        jsonObject.put(Address.CITY, city)
        jsonObject.put(Address.STREET_NAME, streetName)
        jsonObject.put(Address.HOUSE_NUMBER, houseNo)
        jsonObject.put(Address.TYPE, type)
        jsonObject.put(Config.USER_ID, getCurrentUserId()!!)

        val request = JsonObjectRequest(
                Request.Method.PUT,
                Config.getAddressUrl(addressId),
                jsonObject,
                {
                    val data = it.getJSONObject(Config.DATA)
                    val pinCode = data.getInt(Address.PIN_CODE)
                    val responseCity = data.getString(Address.CITY)
                    val responseStreetName = data.getString(Address.STREET_NAME)
                    val houseNumber = data.getString(Address.HOUSE_NUMBER)
                    val responseType = data.getString(Address.TYPE)
                    val userId = data.getString(Config.USER_ID)
                    val id = data.getString(Address.ID)
                    val v = data.getInt(Address.V)
                    mutableLiveData.value = addAddress(AddressWithNameAndPhone(id, name, phone,
                            houseNumber, responseStreetName, responseCity, country, responseType,
                            userId, pinCode, v))
                },
                {
                    it.printStackTrace()
                    if(it.message != null){
                        listener.onNetworkError(it.message!!)
                    }else{
                        listener.onNetworkError("We could not save your address!")
                    }
                    mutableLiveData.value = false
                }
        )
        requestQueue.add(request)

        return mutableLiveData
    }

    fun deleteAddress(addressId: String) : LiveData<Boolean> {
        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()
        val request = StringRequest(
                Request.Method.DELETE,
                Config.getAddressUrl(addressId),
                {
                    deleteUserAddress(addressId)
                    mutableLiveData.value = true
                },
                {
                    it.printStackTrace()
                    mutableLiveData.value = false
                }
        )
        requestQueue.add(request)
        return mutableLiveData
    }

    fun postOrderAndSavedToDatabase(makeOrder: MakeOrder, listener: NetworkErrorListener) : LiveData<Boolean> {

        val mutableLiveData:MutableLiveData<Boolean> = MutableLiveData()

        val orderSummaryJSON = JSONObject()
        orderSummaryJSON.put(OrderSummary.ourPrice, makeOrder.orderSummary.ourPrice)
        orderSummaryJSON.put(OrderSummary.discount, makeOrder.orderSummary.discount)
        orderSummaryJSON.put(OrderSummary.totalAmount, makeOrder.orderSummary.totalAmount)
        orderSummaryJSON.put(OrderSummary.orderAmount, makeOrder.orderSummary.orderAmount)
        orderSummaryJSON.put(OrderSummary.deliveryCharges,  makeOrder.orderSummary.deliveryCharges)

        val userJSON = JSONObject()
        userJSON.put(UserMakingOrder.name, makeOrder.user.name)
        userJSON.put(UserMakingOrder.email, makeOrder.user.email)
        userJSON.put(UserMakingOrder.mobile, makeOrder.user.mobile)

        val shippingAddressJSON = JSONObject()
        shippingAddressJSON.put(ShippingAddress.city, makeOrder.shippingAddress.city)
        shippingAddressJSON.put(ShippingAddress.pincode, makeOrder.shippingAddress.pincode)
        shippingAddressJSON.put(ShippingAddress.houseNo, makeOrder.shippingAddress.houseNo)
        shippingAddressJSON.put(ShippingAddress.streetName, makeOrder.shippingAddress.streetName)


        val paymentJSON = JSONObject()
        paymentJSON.put(Payment.paymentMode, makeOrder.payment.paymentMode)
        paymentJSON.put(Payment.paymentStatus, makeOrder.payment.paymentStatus)

        val productsJSON = JSONArray()

        for(i in 0 until makeOrder.products.size){
            val temp = JSONObject()
            temp.put(Products.mrp, makeOrder.products[i].mrp)
            temp.put(Products.price, makeOrder.products[i].price)
            temp.put(Products.image, makeOrder.products[i].image)
            temp.put(Products.quantity, makeOrder.products[i].quantity)
            productsJSON.put(temp)
        }

        val orderJSONObject = JSONObject()
        orderJSONObject.put(MakeOrder.USER_ID, getCurrentUserId())
        orderJSONObject.put(MakeOrder.ORDER_SUMMARY, orderSummaryJSON)
        orderJSONObject.put(MakeOrder.USER, userJSON)
        orderJSONObject.put(MakeOrder.SHIPPING_ADDRESS, shippingAddressJSON)
        orderJSONObject.put(MakeOrder.PAYMENT, paymentJSON)
        orderJSONObject.put(MakeOrder.PRODUCT, productsJSON)

        val request = JsonObjectRequest(
                Request.Method.POST,
                Config.getCreateOrdersUrl(),
                orderJSONObject,
                {
                    val data = it.getJSONObject(Config.DATA)
                    val orderId = data.getString(Order.id)
                    val userId = data.getString(Order.userId)
                    val orderSummaryData = data.getJSONObject(Order.orderSummary)
                    val userData = data.getJSONObject(Order.user)
                    val shippingAddressData = data.getJSONObject(Order.shippingAddress)
                    val paymentData = data.getJSONObject(Order.payment)
                    val productsData = data.getJSONArray(Order.products)
                    val dates = data.getString(Order.date)
                    val v = data.getInt(Order.v)

                    val orderSummaryId = orderSummaryData.getString(OrderSummary.id)
                    val totalAmount = orderSummaryData.getDouble(OrderSummary.totalAmount).toFloat()
                    val ourPrice = orderSummaryData.getDouble(OrderSummary.ourPrice).toFloat()
                    val discount = orderSummaryData.getDouble(OrderSummary.discount).toFloat()
                    val deliveryCharges = orderSummaryData.getDouble(OrderSummary.deliveryCharges).toFloat()
                    val orderAmount = orderSummaryData.getDouble(OrderSummary.orderAmount).toFloat()

                    val email = userData.getString(UserMakingOrder.email)
                    val mobile = userData.getString(UserMakingOrder.mobile)
                    val name = userData.getString(UserMakingOrder.name)

                    val pincode = shippingAddressData.getInt(ShippingAddress.pincode)
                    val houseNo = shippingAddressData.getString(ShippingAddress.houseNo)
                    val streetName = shippingAddressData.getString(ShippingAddress.streetName)
                    val city = shippingAddressData.getString(ShippingAddress.city)

                    val paymentId = paymentData.getString(Payment.id)
                    val paymentMode = paymentData.getString(Payment.paymentMode)
                    val paymentStatus = paymentData.getString(Payment.paymentStatus)

                    val products = ArrayList<Products>()

                    for(i in 0 until productsData.length()){
                        val product = productsData.getJSONObject(i)
                        val id = product.getString(Products.id)
                        val mrp = product.getDouble(Products.mrp)
                        val price = product.getDouble(Products.price)
                        val image = product.getString(Products.image)
                        val quantity = product.getInt(Products.quantity)

                        addOrderedProducts(orderId, Products(id, mrp, price, quantity, image))
                    }

                    val orderSum = OrderSummary(orderSummaryId, totalAmount, ourPrice, discount, deliveryCharges, orderAmount)
                    val userOrder = UserMakingOrder(userId, email, mobile, name)
                    val shippingAdd = ShippingAddress(pincode, houseNo, streetName, city)
                    val payment = Payment(paymentId, paymentMode, paymentStatus)

                    val orderSummarySuccess = addOrderSummary(orderId, orderSum)
                    val orderSuccess = addOrder(Order(orderId, userId, orderSum, userOrder, shippingAdd, payment, products, dates, v))

                    deleteUserShoppingCart()
                    mutableLiveData.value = (orderSummarySuccess && orderSuccess)

                },
                {
                    it.printStackTrace()
                    if(it.message != null){
                        listener.onNetworkError(it.message!!)
                    }else{
                        listener.onNetworkError("We could not save your address!")
                    }
                    mutableLiveData.value = false
                }
        )
        requestQueue.add(request)

        return mutableLiveData
    }


    private fun isCategoryTableFresh() : Boolean {
        return db.isCategoryTableFresh()
    }

    private fun isSubcategoryTableFresh(catId: Int) : Boolean {
        return db.isSubcategoryTableFresh(catId)
    }

    private fun isProductTableFresh(subId: Int) : Boolean{
        return db.isProductTableFresh(subId)
    }

    private fun addCategory(category: Category){
        db.addCategory(category)
    }

    private fun addSubcategory(subCategory: SubCategory){
        db.addSubcategory(subCategory)
    }

    private fun addProduct(product: Product){
        db.addProduct(product)
    }

    private fun addUser(user: User){
        db.addUser(user)
    }

    private fun addAddress(address: AddressWithNameAndPhone) : Boolean {
        return db.addAddress(address)
    }

    private fun addOrderedProducts(orderId: String, product: Products){
        db.addOrderedProduct(orderId, product)
    }

    fun addProductToUserCart(product: ProductDetails, quantity:Int) : Boolean{
        return db.addProductToUserCart(getCurrentUserId()!!, product, quantity)
    }

    fun addProductToUserCart(product: ProductLight, quantity:Int) : Boolean{
        return db.addProductToUserCart(getCurrentUserId()!!, product, quantity)
    }
    fun getAllCategory() : ArrayList<CategoryLight> {
        return db.getAllCategory()
    }

    fun getAllSubcategoryByCatId(id: Int) : ArrayList<SubcategoryLight> {
        return db.getAllSubcategoryByCatId(id)
    }

    fun getProductById(id: String) : ProductDetails {
        return db.getProductById(id)
    }

    fun getAllProductBySubId(id: Int) : ArrayList<ProductLight> {
        return db.getAllProductBySubId(id)
    }

    fun getAllProductInUserCart() : ArrayList<ProductLight> {
        return db.getAllProductsInUserCart(getCurrentUserId()!!)
    }

    fun getAllAddressWithNameAndPhoneForUser() : ArrayList<AddressWithNameAndPhone> {
        return db.getAllAddressWithNameAndPhoneForUser(getCurrentUserId()!!)
    }

    private fun updateProduct(product: Product){
        db.updateProduct(product)
    }

    fun getUser() = db.getUser(getCurrentUserId()!!)

    fun deleteProductInUserCart(productId: String) : LiveData<Boolean> {
        return db.deleteProductFromCart(getCurrentUserId()!!, productId)
    }

    private fun deleteUserShoppingCart() {
        db.deleteShoppingCart(getCurrentUserId()!!)
    }

    private fun deleteUserAddress(addressId: String) : LiveData<Boolean> {
        return db.deleteUserAddress(getCurrentUserId()!!, addressId)
    }

    private fun addOrder(order: Order) : Boolean {
        return db.addOrder(order)
    }

    private fun addOrderSummary(orderId: String, orderSummary: OrderSummary) : Boolean {
        return db.addOrderSummary(orderId, orderSummary)
    }

    fun getAllOrders() : ArrayList<UserOrder> {
        return db.getAllOrdersBelongingToUser(getCurrentUserId()!!)
    }
}