package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.product.ProductLight

class PaymentHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)

    fun getUserShoppingCart() : ArrayList<ProductLight> {
        return repository.getAllProductInUserCart()
    }
}