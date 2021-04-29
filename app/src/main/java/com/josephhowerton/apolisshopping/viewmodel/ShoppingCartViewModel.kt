package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight

class ShoppingCartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)


    fun getUserShoppingCart() : ArrayList<ProductLight> {
        return repository.getAllProductInUserCart()
    }

    fun addToUserCart(product: ProductLight, quantity: Int) : Boolean{
        return repository.addProductToUserCart(product, quantity)
    }

    fun deleteToUserCart(productId: String) : LiveData<Boolean> {
        return repository.deleteProductInUserCart(productId)
    }
}