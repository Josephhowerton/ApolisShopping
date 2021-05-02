package com.josephhowerton.apolisshopping.model.orders

import java.io.Serializable

data class OrderResponse(
        val error: Boolean,
        val count: Int,
        val data: Order,
)

data class Order(
        val _id: String,
        val userId: String,
        val orderSummary: OrderSummary,
        val user: UserMakingOrder,
        val shippingAddress: ShippingAddress,
        val payment: Payment,
        val products: ArrayList<Products>,
        val date: String,
        val __v: Int

) : Serializable {
    companion object {
        const val id = "_id"
        const val userId = "userId"
        const val orderSummary = "orderSummary"
        const val user = "user"
        const val shippingAddress = "shippingAddress"
        const val payment = "payment"
        const val products = "products"
        const val date = "date"
        const val v = "__v"
    }
}

data class OrderSummary(
        val _id: String,
        val totalAmount: Number,
        val ourPrice: Number,
        val discount: Number,
        val deliveryCharges: Number,
        val orderAmount: Number,
) : Serializable {
    companion object {
        const val id = "_id"
        const val totalAmount = "totalAmount"
        const val ourPrice = "ourPrice"
        const val discount = "discount"
        const val deliveryCharges = "deliveryCharges"
        const val orderAmount = "orderAmount"
    }
}

data class UserMakingOrder(
        val _id: String,
        val email: String,
        val mobile: String,
        val name: String,
) : Serializable {
    companion object {
        const val id = "_id"
        const val email = "email"
        const val mobile = "mobile"
        const val name = "name"
    }
}

data class ShippingAddress(
        val pincode: Int,
        val houseNo: String,
        val streetName: String,
        val city: String,
) : Serializable {
    companion object {
        const val pincode = "pincode"
        const val houseNo = "houseNo"
        const val streetName = "streetName"
        const val city = "city"
    }
}

data class Payment(
        val _id: String,
        val paymentMode: String,
        val paymentStatus: String,
) : Serializable {
    companion object {
        const val id = "_id"
        const val paymentMode = "paymentMode"
        const val paymentStatus = "paymentStatus"
    }
}

data class Products(
        val _id: String,
        val mrp: Number,
        val price: Number,
        val quantity: Int,
        val image: String,
) : Serializable {
    companion object {
        const val id = "_id"
        const val mrp = "mrp"
        const val price = "price"
        const val quantity = "quantity"
        const val image = "image"
    }
}