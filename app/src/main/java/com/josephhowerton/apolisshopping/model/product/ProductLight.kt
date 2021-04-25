package com.josephhowerton.apolisshopping.model.product

import java.io.Serializable

data class ProductLight(
        var productId:String,
        var productName:String,
        var productImage:String,
        var productPrice:Int,
) : Serializable
