package com.josephhowerton.apolisshopping.model.subcategory

import java.io.Serializable

data class SubcategoryLight(
        val id:String,
        val subcategoryId:Int,
        val subcategoryName:String,
        val subcategoryImage:String
): Serializable {
    companion object {
        val SUB_CATEGORY_KEY = "SUB_CATEGORY"
    }
}
