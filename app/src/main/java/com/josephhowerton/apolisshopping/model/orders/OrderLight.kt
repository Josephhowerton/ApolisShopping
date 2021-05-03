package com.josephhowerton.apolisshopping.model.orders

data class OrderLight(
    val orderId: String,
    val userId: String,
    val name: String,
    val paymentMode: String,
    val paymentStatus: String,
    val price: Number,
)