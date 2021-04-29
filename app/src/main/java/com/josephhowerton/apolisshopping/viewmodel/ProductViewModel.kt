package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    fun fetchProducts(id: Int) : LiveData<Boolean> {
        return repository.fetchProducts(id)
    }

    fun fetchProductsDetails(id:String) : LiveData<Boolean>{
        return repository.fetchProductsDetails(id)
    }

    fun addToCart(product: ProductLight){
        repository.addProductToUserCart(product, 1)
    }

    fun getAllProductBySubId(id:Int) : ArrayList<ProductLight> {
        return repository.getAllProductBySubId(id)
    }
}