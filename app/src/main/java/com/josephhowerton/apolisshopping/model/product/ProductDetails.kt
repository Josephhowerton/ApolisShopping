package com.josephhowerton.apolisshopping.model.product

import java.io.Serializable

data class ProductDetails(
        var productId: String,
        var productName: String,
        var productImage: String,
        var productDescription: String,
        var productPrice: Number,
        var productQuantity: Int,
        var subId: Int,
        var catId: Int,
) : Serializable
