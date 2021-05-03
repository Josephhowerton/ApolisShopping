package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.orders.Order
import com.josephhowerton.apolisshopping.model.orders.UserOrder

class OrdersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(application)

    fun getAllOrders(): ArrayList<UserOrder> {
        return repository.getAllOrders()
    }
}
