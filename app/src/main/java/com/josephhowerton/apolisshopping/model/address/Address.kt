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
        const val HOUSE_NUMBER = "houseNo"
        const val ID = "_id"
        const val STREET_NAME = "streetName"
        const val CITY = "city"
        const val TYPE = "type"
        const val USER_ID = "userId"
        const val PIN_CODE = "pincode"
        const val V = "__v"
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
