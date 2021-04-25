package com.josephhowerton.apolisshopping.model.category

import java.io.Serializable

data class CategoryLight(
        val id:String,
        val categoryId:Int,
        val categoryName:String,
        val categoryImage:String
) : Serializable {
        companion object {
                val CATEGORY_ID = "CATEGORY_ID"
        }
}
