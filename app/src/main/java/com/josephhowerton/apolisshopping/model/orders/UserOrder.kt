package com.josephhowerton.apolisshopping.model.orders

import java.io.Serializable

data class UserOrder(
        val orderId: String,
        val userId: String,
        val email: String,
        val mobile: String,
        val name: String,
        val pincode: Int,
        val houseNo: String,
        val streetName: String,
        val city: String,
        val paymentMode: String,
        val paymentStatus: String,
        val date: String,
        val __v: Int,
        val orderSummary: OrderSummary,
        val products: ArrayList<Products>,
) : Serializable {
        companion object {
                val ORDER = "order"
        }
}
