package com.josephhowerton.apolisshopping.app

class Config {
    companion object{
        const val SHARED_PREFERENCE_NAME = "DEFAULT"
        const val CURRENT_USER = "CURRENT_USER"
        const val ACTION_PAYMENT = "action_payment_info"
        const val ACTION_ADDRESS = "action_address"
        const val ACTION_ORDER = "action_order"
        const val BANNER_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUo8ynBItVwCdYRCNj9cibTm2nud8gACmfsA&usqp=CAU"
        const val EDIT_TYPE_KEY = "EDIT_TYPE"
        const val DATA = "data"
        const val USER_ID = "userId"

        fun getBaseUrlWithEndpoint(endpoint:String): String{
            return "http://grocery-second-app.herokuapp.com/api/${endpoint}?"
        }

        fun getBaseUrlWithEndpoint(endpoint:String, id:Int): String{
            return "http://grocery-second-app.herokuapp.com/api/${endpoint}/${id}?"
        }

        fun getBaseUrlWithProductEndpoint(endpoint:String, id:Int): String{
            return "http://grocery-second-app.herokuapp.com/api/${endpoint}/sub/${id}?"
        }

        fun getBaseUrlWithProductDetailsEndpoint(endpoint:String, id:String): String{
            return "http://grocery-second-app.herokuapp.com/api/${endpoint}/${id}?"
        }

        fun getImageUrlWithCategoryId(categoryId:String): String{
            return "http://rjtmobile.com/grocery/images/${categoryId}?"
        }

        fun getSignInUrl(): String {
            return "http://grocery-second-app.herokuapp.com/api/${Endpoint.URL_SIGN_IN}?"
        }

        fun getSignUpUrl(): String {
            return "http://grocery-second-app.herokuapp.com/api/${Endpoint.URL_REGISTER}?"
        }

        fun getAddressUrl(): String {
            return "http://grocery-second-app.herokuapp.com/api/${Endpoint.ADDRESS}"
        }

        fun getAddressUrl(addressId: String): String {
            return "http://grocery-second-app.herokuapp.com/api/${Endpoint.ADDRESS}/${addressId}"
        }

        fun getOrdersUrl(userId: String): String {
            return "http://grocery-second-app.herokuapp.com/api/orders/${userId}"
        }

        fun getCreateOrdersUrl(): String {
            return "http://grocery-second-app.herokuapp.com/api/orders/"
        }

        const val TITLE = "There was an error!"
        const val MESSAGE = "There was an error finding the data from the network. Check your internet connection and try again later."
        const val BTN = "Try Later"

        enum class EDIT_TYPE{
            EDIT,
            ADD
        }

    }
}