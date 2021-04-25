package com.josephhowerton.apolisshopping.model.product

import java.io.Serializable

data class ProductResponse (
    val error: Boolean,
    val count: Int,
    val data: ArrayList<Product>
)

data class Product (
    val quantity:Int,
    val description: String,
    val status: Boolean,
    val position: Int,
    val created: String,
    val _id: String,
    val catId: Int,
    val subId: Int,
    val productName: String,
    val image: String,
    val unit: String,
    val price: Int,
    val mrp: Int,
    val __v:Int
) : Serializable