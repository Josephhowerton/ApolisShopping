package com.josephhowerton.apolisshopping.model

import java.io.Serializable

data class Address(
    val _id: String,
    val houseNo: String,
    val streetName: String,
    val city: String,
    val type: String,
    val userId: String,
    val pincode: Int,
    val __v: Int,
) : Serializable {
    companion object{
        const val ADDRESS = "Address"
    }
}

data class AddressWithNameAndPhone(
        val _id: String,
        val name: String,
        val mobile: String,
        val houseNo: String,
        val streetName: String,
        val city: String,
        val country: String,
        val type: String,
        val userId: String,
        val pincode: Int,
        val __v: Int,
) : Serializable {
    companion object{
        const val ADDRESS = "Address"
    }
}
