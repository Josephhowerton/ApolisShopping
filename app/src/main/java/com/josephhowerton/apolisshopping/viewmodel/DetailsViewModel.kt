package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    fun getProductDetails(id:String) : ProductDetails{
        return repository.getProductById(id);
    }

    fun addToUserCart(product: ProductDetails, quantity: Int){
        repository.addProductToUserCart(product, quantity)
    }
}