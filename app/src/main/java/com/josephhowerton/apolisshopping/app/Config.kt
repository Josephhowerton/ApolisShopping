package com.josephhowerton.apolisshopping.app

class Config {
    companion object{
        val BANNER_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUo8ynBItVwCdYRCNj9cibTm2nud8gACmfsA&usqp=CAU"

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

        fun getImageUrlWithCategoryId(categoryId:Int): String{
            return "http://rjtmobile.com/grocery/images/${categoryId}?"
        }

        fun getImageUrlWithCategoryId(categoryId:String): String{
            return "http://rjtmobile.com/grocery/images/${categoryId}?"
        }

        val TITLE = "There was an error!"
        val MESSAGE = "There was an error finding the data from the network. Check your internet connection and try again later."
        val BTN = "Try Later"

    }
}