package com.josephhowerton.apolisshopping.model.orders

import java.io.Serializable

data class MakeOrder(
        val userId: String,
        val orderSummary: OrderInfo,
        val user: UserInfo,
        val shippingAddress: ShippingInfo,
        val payment: PaymentInfo,
        val products: ArrayList<ProductsInfo>,
) : Serializable {
    companion object {
        const val USER_ID = "userId"
        const val ORDER_SUMMARY = "orderSummary"
        const val USER = "user"
        const val SHIPPING_ADDRESS = "shippingAddress"
        const val PAYMENT = "payment"
        const val PRODUCT = "products"
    }
}

data class OrderInfo(
        val totalAmount: Number,
        val ourPrice: Number,
        val discount: Number,
        val deliveryCharges: Number,
        val orderAmount: Number,
)

data class UserInfo(
        val email: String,
        val mobile: String,
        val name: String,
)

data class ShippingInfo(
        val pincode: Int,
        val houseNo: String,
        val streetName: String,
        val city: String,
)

data class PaymentInfo(
        val paymentMode: String,
        val paymentStatus: String,
)

data class ProductsInfo(
        val mrp: Number,
        val price: Number,
        val quantity: Int,
        val image: String,
)
