package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.*
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight

class MainViewModel(application:Application): AndroidViewModel (application){

    private val repository:Repository = Repository(application)


    fun fetchCategory(){
        repository.fetchCategory()
    }

    fun fetchSubCategory(id:Int){
        repository.fetchSubCategory(id)
    }

    fun fetchProducts(id: Int){
        repository.fetchProducts(id)
    }

    fun fetchItemDetails(id:String){
        repository.fetchItemDetails(id)
    }

    fun getAllCategory() : ArrayList<CategoryLight> {
        return repository.getAllCategory()
    }

    fun getAllSubcategoryByCatId(id:Int) : ArrayList<SubcategoryLight> {
        return repository.getAllSubcategoryByCatId(id)
    }

    fun getProductById(id: String) : ProductDetails {
        return repository.getProductById(id)
    }

    fun getAllProductBySubId(id:Int) : ArrayList<ProductLight> {
        return repository.getAllProductBySubId(id)
    }

}