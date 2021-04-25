package com.josephhowerton.apolisshopping.model.subcategory

import java.io.Serializable

data class SubCategoryResponse(
        val count:Int,
        val data:ArrayList<SubCategory>,
        val error:Boolean
)

data class SubCategory(
    val subImage:String,
    val subDescription:String,
    val status:Boolean,
    val position: Int,
    val _id:String,
    val catId:Int,
    val subId:Int,
    val subName: String,
    val __v:Int,
) : Serializable {
    companion object {
        val SUB_CATEGORY_KEY = "SUB_CATEGORY"
    }
}