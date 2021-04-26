package com.josephhowerton.apolisshopping

import android.app.Application
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

    private val requestQueue:RequestQueue = Volley.newRequestQueue(application)
    private val gson:Gson = Gson()

    private val db: DBHelper = DBHelper(application)

    fun init() : LiveData<Boolean> {
        return fetchCategory()
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
                    mutableLiveData.value = true
                    val userName = it.getString(DBHelper.FIRST_NAME)
                    val userEmail = it.getString(DBHelper.EMAIL)
                    val userPassword = it.getString(DBHelper.PASSWORD)
                    val userPhone = it.getString(DBHelper.MOBILE)
                    val userV = it.getInt(DBHelper.V)
                    val userId = it.getString(DBHelper.ID)
                    val userCreatedAt = it.getString(DBHelper.CREATED_AT)
                    val user = User(userName, userId, userEmail, userPassword, userPhone, userCreatedAt, userV)
                    updateUser(user)
                },
                {
                    it.printStackTrace()
                    listener.onNetworkError(it.message!!)
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
//                val userName = it.getString(DBHelper.FIRST_NAME)
//                val userEmail = it.getString(DBHelper.EMAIL)
//                val userPassword = it.getString(DBHelper.PASSWORD)
//                val userPhone = it.getString(DBHelper.MOBILE)
//                val userV = it.getInt(DBHelper.V)
//                val userId = it.getString(DBHelper.ID)
//                val userCreatedAt = it.getString(DBHelper.CREATED_AT)
//                val user = User(userName, userId, userEmail, userPassword, userPhone, userCreatedAt, userV)
//                addUser(user)
                print(it.toString())
//                mutableLiveData.value = true
            },
            {
                it.printStackTrace()
                listener.onNetworkError("Error Signing In")
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

    private fun updateProduct(product: Product){
        db.updateProduct(product)
    }

    private fun updateUser(user: User){
        db.updateUser(user)
    }
}