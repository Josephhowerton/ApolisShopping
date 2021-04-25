package com.josephhowerton.apolisshopping.model.product

import java.io.Serializable

data class ProductDetails(
        var productId:String,
        var productName:String,
        var productImage:String,
        var productDescription:String,
        var productPrice:Int,
        var productQuantity:Int,
) : Serializable
