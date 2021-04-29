package com.josephhowerton.apolisshopping

import android.app.Application
import android.content.Context
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
import com.josephhowerton.apolisshopping.model.product.*
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubCategoryResponse
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.model.user.User
import org.json.JSONObject

class Repository constructor(application: Application){
    private val sharedPreferences = application.getSharedPreferences(Config.SHARED_PREFERENCE_NAME, AppCompatActivity.MODE_PRIVATE)
    private val requestQueue:RequestQueue = Volley.newRequestQueue(application)
    private val gson:Gson = Gson()
    private val db: DBHelper = DBHelper(application)

    private lateinit var user:User

    fun init() : LiveData<Boolean> {
        return fetchCategory()
    }

    fun initUser(){
        val userId = getCurrentUserId()
        if(userId != null){
            user = db.getUser(userId)
        }else{
            println("ERROR ASSIGNING USER ")
        }
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

        jsonObject.put(DBHelper.PIN_CODE, pincode)
        jsonObject.put(DBHelper.CITY, city)
        jsonObject.put(DBHelper.STREET_NAME, streetName)
        jsonObject.put(DBHelper.HOUSE_NUMBER, houseNo)
        jsonObject.put(DBHelper.TYPE, type)
        jsonObject.put("userId", getCurrentUserId()!!)

        val request = JsonObjectRequest(
            Request.Method.POST,
            Config.getAddressUrl(),
            jsonObject,
            {
                val data = it.getJSONObject("data")
                val pinCode = data.getInt(DBHelper.PIN_CODE)
                val responseCity = data.getString(DBHelper.CITY)
                val responseStreetName = data.getString(DBHelper.STREET_NAME)
                val houseNumber = data.getString(DBHelper.HOUSE_NUMBER)
                val responseType = data.getString(DBHelper.TYPE)
                val userId = data.getString("userId")
                val id = data.getString(DBHelper.ID)
                val v = data.getInt(DBHelper.V)
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

        jsonObject.put(DBHelper.PIN_CODE, pincode)
        jsonObject.put(DBHelper.CITY, city)
        jsonObject.put(DBHelper.STREET_NAME, streetName)
        jsonObject.put(DBHelper.HOUSE_NUMBER, houseNo)
        jsonObject.put(DBHelper.TYPE, type)
        jsonObject.put("userId", getCurrentUserId()!!)

        val request = JsonObjectRequest(
                Request.Method.PUT,
                Config.getAddressUrl(addressId),
                jsonObject,
                {
                    val data = it.getJSONObject("data")
                    val pinCode = data.getInt(DBHelper.PIN_CODE)
                    val responseCity = data.getString(DBHelper.CITY)
                    val responseStreetName = data.getString(DBHelper.STREET_NAME)
                    val houseNumber = data.getString(DBHelper.HOUSE_NUMBER)
                    val responseType = data.getString(DBHelper.TYPE)
                    val userId = data.getString("userId")
                    val id = data.getString(DBHelper.ID)
                    val v = data.getInt(DBHelper.V)
                    mutableLiveData.value = addAddress(AddressWithNameAndPhone(name, phone, id,
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

    fun getAllAddressForUser() : ArrayList<Address> {
        return db.getAllAddressForUser(getCurrentUserId()!!)
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

    private fun deleteUserAddress(addressId: String) : LiveData<Boolean> {
        return db.deleteUserAddress(getCurrentUserId()!!, addressId)
    }
}