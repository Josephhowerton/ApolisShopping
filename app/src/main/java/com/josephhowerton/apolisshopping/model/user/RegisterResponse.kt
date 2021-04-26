package com.josephhowerton.apolisshopping.model.user

data class RegisterResponse(
        val error:Boolean,
        val message: String,
        val data: User
)
