package com.josephhowerton.apolisshopping.model.user

import java.io.Serializable

data class User(
        val firstName:String,
        val _id:String,
        val email:String,
        val password:String,
        val mobile:String,
        val createdAt:String,
        val __v:Int
) : Serializable {
    companion object {
        const val FIRST_NAME = "firstName"
        const val ID = "_id"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val MOBILE = "mobile"
        const val CREATED_AT = "createdAt"
        const val V = "__v"
    }
}
