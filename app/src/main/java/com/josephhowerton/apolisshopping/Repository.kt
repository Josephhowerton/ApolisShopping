package com.josephhowerton.apolisshopping

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.app.Endpoint
import com.josephhowerton.apolisshopping.database.DBHelper
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.category.CategoryResponse
import com.josephhowerton.apolisshopping.model.product.*
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubCategoryResponse
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight

class Repository constructor(application: Application){

    private val requestQueue:RequestQueue = Volley.newRequestQueue(application)
    private val gson:Gson = Gson()

    private val db: DBHelper = DBHelper(application)

    fun init() : LiveData<Boolean> {
        return fetchCategory()
    }

    public fun isCurrentUser() : Boolean{
        TODO("return if any user is signed in")
    }

    public fun authenticate(username:String, password:String){
        
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

    fun updateProduct(product: Product){
        db.updateProduct(product)
    }
}