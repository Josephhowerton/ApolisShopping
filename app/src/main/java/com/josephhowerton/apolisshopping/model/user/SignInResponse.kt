package com.josephhowerton.apolisshopping.model.user

data class SignInResponse(
        val token:String,
        val user:User
)
