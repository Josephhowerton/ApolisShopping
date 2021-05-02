package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.orders.MakeOrder
import com.josephhowerton.apolisshopping.model.orders.UserInfo
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.user.User

class PaymentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)

    fun getUserShoppingCart() : ArrayList<ProductLight> {
        return repository.getAllProductInUserCart()
    }

    fun getUser() : User {
        return repository.getUser()
    }

    fun createOrderAndSaveInDatabase(order: MakeOrder, listener: NetworkErrorListener) : LiveData<Boolean> {
        return repository.postOrderAndSavedToDatabase(order, listener)
    }
}