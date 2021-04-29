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

    fun initUser(){
        repository.initUser()
    }

    fun fetchSubCategory(id:Int)  : LiveData<Boolean>{
        return repository.fetchSubCategory(id)
    }

    fun getAllCategory() : ArrayList<CategoryLight> {
        return repository.getAllCategory()
    }
}