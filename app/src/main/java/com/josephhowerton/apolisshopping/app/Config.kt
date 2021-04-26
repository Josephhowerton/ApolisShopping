package com.josephhowerton.apolisshopping.app

class Config {
    companion object{
        val BANNER_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUo8ynBItVwCdYRCNj9cibTm2nud8gACmfsA&usqp=CAU"
        val BASE_URL = "http://grocery-second-app.herokuapp.com/api/"
        val BASE_IMAGE_URL = "http://rjtmobile.com/grocery/images/"

        fun getBaseUrlWithEndpoint(endpoint:String): String{
            return "${"$BASE_URL$endpoint/$id"}?"
        }

        fun getBaseUrlWithEndpoint(endpoint:String, id:Int): String{
            return "${"$BASE_URL$endpoint/$id"}?"
        }

        fun getBaseUrlWithProductEndpoint(endpoint:String, id:Int): String{
            return "${"$BASE_URL$endpoint/sub/$id"}?"
        }

        fun getBaseUrlWithProductDetailsEndpoint(endpoint:String, id:String): String{
            return "${"$BASE_URL$endpoint/$id"}?"
        }

        fun getImageUrlWithCategoryId(categoryId:Int): String{
            return "${"$BASE_IMAGE_URL$categoryId"}?"
        }

        fun getImageUrlWithCategoryId(categoryId:String): String{
            return "${"$BASE_IMAGE_URL$categoryId"}?"
        }

        fun getRegister(): String{
            return "$BASE_URL + ${Endpoint.URL_REGISTER}"
        }

        val TITLE = "There was an error!"
        val MESSAGE = "There was an error finding the data from the network. Check your internet connection and try again later."
        val BTN = "Try Later"

    }
}