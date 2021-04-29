package com.josephhowerton.apolisshopping.interfaces

import com.josephhowerton.apolisshopping.model.product.ProductLight

interface AdjustQuantityListener {
    fun onAddQuantityClickListener(product: ProductLight, position: Int)
    fun onSubtractQuantityClickListener(product: ProductLight, position: Int)
    fun onDeleteIfQuantityIsZero(product: ProductLight, position: Int)
}