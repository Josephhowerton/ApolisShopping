package com.josephhowerton.apolisshopping.interfaces

import com.josephhowerton.apolisshopping.model.product.ProductLight

interface AddToCartListener {
    fun onAddToCart(productLight: ProductLight)
}