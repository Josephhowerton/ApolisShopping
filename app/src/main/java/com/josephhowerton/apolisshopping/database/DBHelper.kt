package com.josephhowerton.apolisshopping.database

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.orders.Order
import com.josephhowerton.apolisshopping.model.orders.OrderSummary
import com.josephhowerton.apolisshopping.model.orders.Products
import com.josephhowerton.apolisshopping.model.orders.UserOrder
import com.josephhowerton.apolisshopping.model.product.Product
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.model.user.User
import java.lang.Exception

class  DBHelper(application: Application) : SQLiteOpenHelper(application, DB_NAME, null, VERSION){

    companion object {
        const val DB_NAME = "APOLIS_SHOPPING"
        const val VERSION = 5

        const val CATEGORY_TABLE_NAME = "category"
        const val SUBCATEGORY_TABLE_NAME = "subcategory"
        const val PRODUCT_TABLE_NAME = "product"
        const val ORDERED_PRODUCTS_TABLE_NAME = "ordered_products"
        const val USER_TABLE_NAME = "user"
        const val SHOPPING_CART_TABLE_NAME = "shopping_cart"
        const val ADDRESS_TABLE_NAME = "address"
        const val ORDERS_TABLE_NAME = "orders"
        const val ORDERS_SUMMARY_TABLE_NAME = "order_summary"

        const val V = "__v"
        const val ID = "_id"
        const val CAT_DESCRIPTION = "catDescription"
        const val CAT_ID = "cat_id"
        const val CAT_IMAGE = "cat_image"
        const val CAT_NAME = "cat_name"
        const val POSITION = "position"
        const val SLUG = "slug"
        const val STATUS = "status"

        const val SUB_DESCRIPTION = "sub_description"
        const val SUB_IMAGE = "sub_image"
        const val SUB_NAME = "sub_name"
        const val SUB_ID = "sub_id"

        const val QUANTITY = "quantity"
        const val DESCRIPTION = "description"
        const val CREATED = "created"
        const val PRODUCT_NAME = "product_name"
        const val IMAGE = "image"
        const val UNIT = "unit"
        const val PRICE = "price"
        const val MRP = "mrp"

        const val PRODUCT_ID = "product_id"
        const val DATE = "date"

        const val FIRST_NAME = "first_name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val MOBILE = "mobile"
        const val CREATED_AT = "created_at"

        const val USER_ID = "user_id"
        const val ITEM_ID = "item_id"
        const val ADDRESS_ID = "address_id"
        const val ORDER_ID = "order_id"
        const val ORDER_SUMMARY_ID = "order_summary_id"

        const val CITY = "city"
        const val COUNTRY = "country"
        const val PIN_CODE = "pincode"
        const val STREET_NAME = "street_name"
        const val TYPE = "type"
        const val HOUSE_NUMBER = "house_number"

        const val TOTAL_AMOUNT = "total_amount"
        const val OUR_PRICE = "our_price"
        const val DISCOUNT = "discount"
        const val DELIVERY_CHARGES = "delivery_charges"
        const val ORDER_AMOUNT = "order_amount"

        const val EMAIL_USED = "email_used"
        const val MOBILE_USED = "mobile_used"
        const val PAYMENT_MODE = "payment_mode"
        const val PAYMENT_STATUS = "payment_status"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val categoryTable = "create table $CATEGORY_TABLE_NAME ($V integer, $ID varchar(50)," +
                "$CAT_DESCRIPTION varchar (300), $CAT_ID integer, $CAT_IMAGE varchar(50)," +
                "$CAT_NAME varchar(50), $POSITION integer, $SLUG varchar(50), $STATUS boolean, PRIMARY KEY ($ID))"

        val subcategoryTable = "create table $SUBCATEGORY_TABLE_NAME ($V integer, $ID varchar(50)," +
                "$SUB_DESCRIPTION varchar (300), $CAT_ID integer, $SUB_IMAGE varchar(50)," +
                "$SUB_NAME varchar(50), $POSITION integer, $SUB_ID integer, $STATUS boolean, PRIMARY KEY ($ID))"

        val productTable = "create table $PRODUCT_TABLE_NAME ($QUANTITY integer, $DESCRIPTION varchar(300)," +
                "$CREATED varchar(50), $ID varchar(50), $CAT_ID integer, $SUB_ID integer," +
                "$PRODUCT_NAME varchar(50), $IMAGE varchar(50), $UNIT varchar(5), $PRICE real," +
                "$MRP real,$POSITION integer, $STATUS boolean, PRIMARY KEY ($ID))"

        val userTable = "create table $USER_TABLE_NAME ($FIRST_NAME varchar(30)," +
                "$CREATED_AT varchar(50), $ID varchar(50), $EMAIL varchar(30), $PASSWORD varchar(100)," +
                "$MOBILE varchar(20), $V integer, PRIMARY KEY ($ID))"

        val shoppingTable = "create table $SHOPPING_CART_TABLE_NAME ($USER_ID varchar(50), $ITEM_ID varchar(30)," +
                "$QUANTITY varchar(30), $IMAGE varchar(50), $PRICE real, $PRODUCT_NAME varchar(50), PRIMARY KEY ($USER_ID, $ITEM_ID))"

        val addressTable = "create table $ADDRESS_TABLE_NAME ($FIRST_NAME varchar(50), $USER_ID varchar(50), $ADDRESS_ID varchar(30), " +
                "$CITY varchar(30), $COUNTRY varchar(3), $STREET_NAME varchar(50), $PIN_CODE integer, $TYPE varchar(50), " +
                "$HOUSE_NUMBER varchar(50), $V integer, $MOBILE varchar(20), PRIMARY KEY ($USER_ID, $ADDRESS_ID))"

        val orderTable = "create table $ORDERS_TABLE_NAME ($ORDER_ID varchar(50), $USER_ID varchar(50), " +
                "$EMAIL_USED varchar(50), $MOBILE_USED varchar(13), $FIRST_NAME varchar(20), " +
                "$PIN_CODE integer, $HOUSE_NUMBER varchar(50), $STREET_NAME varchar(30), $CITY varchar(20), $PAYMENT_MODE varchar(10), " +
                "$PAYMENT_STATUS varchar(10), $DATE varchar(30), $V integer, PRIMARY KEY ($ORDER_ID, $USER_ID))"

        val orderedProductsTable = "create table $ORDERED_PRODUCTS_TABLE_NAME ($QUANTITY integer, " +
                "$ID varchar(50), $ORDER_ID varchar(30), $PRODUCT_NAME varchar(50), $IMAGE varchar(50), $PRICE real," +
                "$MRP real, PRIMARY KEY ($ID, $ORDER_ID))"

        val orderSummaryTable = "create table $ORDERS_SUMMARY_TABLE_NAME ($ORDER_SUMMARY_ID varchar(30), $ORDER_ID varchar(30)," +
                "$TOTAL_AMOUNT real, $OUR_PRICE real, $DISCOUNT real, $DELIVERY_CHARGES real, " +
                "$ORDER_AMOUNT real, PRIMARY KEY ($ORDER_SUMMARY_ID, $ORDER_ID))"

        db?.execSQL(categoryTable)
        db?.execSQL(subcategoryTable)
        db?.execSQL(productTable)
        db?.execSQL(userTable)
        db?.execSQL(shoppingTable)
        db?.execSQL(addressTable)
        db?.execSQL(orderTable)
        db?.execSQL(orderedProductsTable)
        db?.execSQL(orderSummaryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun isCategoryTableFresh() : Boolean {
        val cursor = readableDatabase.query(CATEGORY_TABLE_NAME, null, null, null, null, null, null)
        val count: Int
        return if(cursor != null){
            count = cursor.count
            cursor.close()
            count > 0
        }else{
            false
        }
    }

    fun isSubcategoryTableFresh(id: Int) : Boolean {
        val selection = "$CAT_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = readableDatabase.query(SUBCATEGORY_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val count: Int
        return if(cursor != null){
            count = cursor.count
            cursor.close()
            count > 0
        }else{
            false
        }
    }

    fun isProductTableFresh(id: Int) : Boolean{
        val selection = "$SUB_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = readableDatabase.query(PRODUCT_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val count: Int
        return if(cursor != null){
            count = cursor.count
            cursor.close()
            count > 0
        }else{
            false
        }
    }

    fun addCategory(category: Category){
        val contentValues = ContentValues()
        contentValues.put(V, category.__v )
        contentValues.put(ID, category._id )
        contentValues.put(CAT_DESCRIPTION, category.catDescription)
        contentValues.put(CAT_ID, category.catId)
        contentValues.put(CAT_IMAGE, category.catImage)
        contentValues.put(CAT_NAME, category.catName)
        contentValues.put(POSITION, category.position)
        contentValues.put(SLUG, category.slug)
        contentValues.put(STATUS, category.status)
        writableDatabase.insert(CATEGORY_TABLE_NAME, null, contentValues)
    }

    fun addSubcategory(subCategory: SubCategory){
        val contentValues = ContentValues()
        contentValues.put(V, subCategory.__v )
        contentValues.put(ID, subCategory._id )
        contentValues.put(SUB_DESCRIPTION, subCategory.subDescription)
        contentValues.put(CAT_ID, subCategory.catId)
        contentValues.put(SUB_IMAGE, subCategory.subImage)
        contentValues.put(SUB_NAME, subCategory.subName)
        contentValues.put(POSITION, subCategory.position)
        contentValues.put(SUB_ID, subCategory.subId)
        contentValues.put(STATUS, subCategory.status)
        writableDatabase.insert(SUBCATEGORY_TABLE_NAME, null, contentValues)
    }

    fun addProduct(product: Product){
        val contentValues = ContentValues()
        contentValues.put(QUANTITY, product.quantity )
        contentValues.put(DESCRIPTION, product.description )
        contentValues.put(CREATED, product.created)
        contentValues.put(ID, product._id)
        contentValues.put(CAT_ID, product.catId)
        contentValues.put(SUB_ID, product.subId)
        contentValues.put(PRODUCT_NAME, product.productName)
        contentValues.put(IMAGE, product.image)
        contentValues.put(UNIT, product.unit)
        contentValues.put(PRICE, product.price.toFloat())
        contentValues.put(MRP, product.mrp.toFloat())
        contentValues.put(POSITION, product.position)
        contentValues.put(STATUS, product.status)
        writableDatabase.insert(PRODUCT_TABLE_NAME, null, contentValues)
    }

    fun addUser(user: User){
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME, user.firstName)
        contentValues.put(ID, user._id)
        contentValues.put(EMAIL, user.email)
        contentValues.put(PASSWORD, user.password)
        contentValues.put(MOBILE, user.mobile)
        contentValues.put(CREATED_AT, user.createdAt)
        contentValues.put(V, user.__v )
        writableDatabase.insert(USER_TABLE_NAME, null, contentValues)
    }

    fun addProductToUserCart(userID: String, product: ProductDetails, quantity: Int) : Boolean{
        val contentValues = ContentValues()
        contentValues.put(USER_ID, userID)
        contentValues.put(ITEM_ID, product.productId)
        contentValues.put(IMAGE, product.productImage)
        contentValues.put(PRODUCT_NAME, product.productName)
        contentValues.put(PRICE, product.productPrice.toFloat())
        contentValues.put(QUANTITY, quantity)
        return try {
            writableDatabase.insertOrThrow(SHOPPING_CART_TABLE_NAME, null, contentValues)
            true
        }catch (e: Exception){
            val productInCart = getProductInUserCart(userID, product.productId)
            productInCart.productQuantity = (productInCart.productQuantity + quantity)
            updateQuantity(userID, productInCart)
            true
        }
    }

    fun addProductToUserCart(userID: String, product: ProductLight, quantity: Int) : Boolean{
        val contentValues = ContentValues()
        contentValues.put(USER_ID, userID)
        contentValues.put(ITEM_ID, product.productId)
        contentValues.put(IMAGE, product.productImage)
        contentValues.put(PRODUCT_NAME, product.productName)
        contentValues.put(PRICE,  product.productPrice.toFloat())
        contentValues.put(QUANTITY, quantity)
        return try {
            writableDatabase.insertOrThrow(SHOPPING_CART_TABLE_NAME, null, contentValues)
            true
        }catch (e: SQLiteConstraintException){
            val productInCart = getProductInUserCart(userID, product.productId)
            productInCart.productQuantity = (productInCart.productQuantity + quantity)
            updateQuantity(userID, productInCart)
            true
        }
    }

    fun addAddress(address: AddressWithNameAndPhone) : Boolean {
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME, address.name)
        contentValues.put(MOBILE, address.mobile)
        contentValues.put(USER_ID, address.userId)
        contentValues.put(ADDRESS_ID, address._id)
        contentValues.put(CITY, address.city)
        contentValues.put(COUNTRY, address.country)
        contentValues.put(HOUSE_NUMBER, address.houseNo)
        contentValues.put(PIN_CODE,  address.pincode)
        contentValues.put(STREET_NAME, address.streetName)
        contentValues.put(V,  address.__v)
        contentValues.put(TYPE, address.type)
        return try {
            writableDatabase.insertOrThrow(ADDRESS_TABLE_NAME, null, contentValues)
            true
        }catch (e: SQLiteConstraintException){
            val whereClause = "$USER_ID = ? AND $ADDRESS_ID = ?"
            val whereValues = arrayOf(address.userId, address._id)
            writableDatabase.update(ADDRESS_TABLE_NAME, contentValues, whereClause, whereValues)
            true
        }
    }

    fun addOrder(order: Order) : Boolean {
        val contentValues = ContentValues()

        contentValues.put(ORDER_ID, order._id)
        contentValues.put(USER_ID, order.userId)
        contentValues.put(EMAIL_USED, order.user.email)
        contentValues.put(MOBILE_USED, order.user.mobile)
        contentValues.put(FIRST_NAME, order.user.name)
        contentValues.put(PIN_CODE, order.shippingAddress.pincode)
        contentValues.put(HOUSE_NUMBER,  order.shippingAddress.houseNo)
        contentValues.put(STREET_NAME, order.shippingAddress.streetName)
        contentValues.put(CITY,  order.shippingAddress.city)
        contentValues.put(PAYMENT_MODE, order.payment.paymentMode)
        contentValues.put(PAYMENT_STATUS, order.payment.paymentStatus)
        contentValues.put(DATE,  order.date)
        contentValues.put(V, order.__v)

        return try {
            writableDatabase.insertOrThrow(ORDERS_TABLE_NAME, null, contentValues)
            true
        }catch (e: SQLiteConstraintException){
            val whereClause = "$USER_ID = ? AND $ORDER_ID = ?"
            val whereValues = arrayOf(order.userId, order._id)
            writableDatabase.update(ORDERS_TABLE_NAME, contentValues, whereClause, whereValues)
            true
        }
    }

    fun addOrderedProduct(orderId: String, product: Products) : Boolean {
        val contentValues = ContentValues()

        contentValues.put(ORDER_ID, orderId)
        contentValues.put(IMAGE, product.image)
        contentValues.put(ID, product._id)
        contentValues.put(MRP, product.mrp.toDouble())
        contentValues.put(QUANTITY, product.quantity)
        contentValues.put(PRICE, product.price.toDouble())

        return try {
            writableDatabase.insertOrThrow(ORDERED_PRODUCTS_TABLE_NAME, null, contentValues)
            true
        }catch (e: SQLiteConstraintException){
            val whereValues = arrayOf(orderId, product._id)
            val whereClause = "$ID = ? AND $ORDER_ID = ?"
            writableDatabase.update(ORDERS_TABLE_NAME, contentValues, whereClause, whereValues)
            true
        }
    }

    fun addOrderSummary(orderId: String, orderSummary: OrderSummary) : Boolean {
        val contentValues = ContentValues()
        contentValues.put(ORDER_SUMMARY_ID, orderSummary._id)
        contentValues.put(ORDER_ID, orderId)
        contentValues.put(TOTAL_AMOUNT, orderSummary.totalAmount.toFloat())
        contentValues.put(OUR_PRICE, orderSummary.ourPrice.toFloat())
        contentValues.put(DISCOUNT, orderSummary.discount.toFloat())
        contentValues.put(DELIVERY_CHARGES, orderSummary.deliveryCharges.toFloat())
        contentValues.put(ORDER_AMOUNT, orderSummary.orderAmount.toFloat())
        return try {
            writableDatabase.insertOrThrow(ORDERS_SUMMARY_TABLE_NAME, null, contentValues)
            true
        }catch (e: SQLiteConstraintException){
            val whereClause = "$ORDER_ID = ? AND $ORDER_SUMMARY_ID = ?"
            val whereValues = arrayOf(orderId, orderSummary._id)
            writableDatabase.update(ORDERS_SUMMARY_TABLE_NAME, contentValues, whereClause, whereValues)
            true
        }
    }

    fun getAllCategory() : ArrayList<CategoryLight> {
        val categories = ArrayList<CategoryLight>()
        val columns = arrayOf(ID, CAT_ID, CAT_NAME, CAT_IMAGE)
        val cursor = readableDatabase.query(CATEGORY_TABLE_NAME, columns, null, null, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(ID))
                val categoryId = cursor.getInt(cursor.getColumnIndex(CAT_ID))
                val categoryName = cursor.getString(cursor.getColumnIndex(CAT_NAME))
                val categoryImage = cursor.getString(cursor.getColumnIndex(CAT_IMAGE))
                categories.add(CategoryLight(id, categoryId, categoryName, categoryImage))
            }while (cursor.moveToNext())

            cursor.close()
        }
        return categories
    }

    fun getAllSubcategoryByCatId(catId: Int) : ArrayList<SubcategoryLight> {
        val subcategories = ArrayList<SubcategoryLight>()
        val selection = "$CAT_ID = ?"
        val selectionArgs = arrayOf(catId.toString())
        val columns = arrayOf(ID, SUB_ID, SUB_NAME, SUB_IMAGE)
        val cursor = readableDatabase.query(SUBCATEGORY_TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(ID))
                val subcategoryId = cursor.getInt(cursor.getColumnIndex(SUB_ID))
                val subcategoryName = cursor.getString(cursor.getColumnIndex(SUB_NAME))
                val subcategoryImage = cursor.getString(cursor.getColumnIndex(SUB_IMAGE))
                subcategories.add(SubcategoryLight(id, subcategoryId, subcategoryName, subcategoryImage))
            }while (cursor.moveToNext())

            cursor.close()
        }
        return subcategories
    }

    fun getProductById(id: String) : ProductDetails {
        val selection = "$ID = ?"
        val selectionArgs = arrayOf(id)
        val columns = arrayOf(ID, PRODUCT_NAME, IMAGE, PRICE, DESCRIPTION, QUANTITY, SUB_ID, CAT_ID)
        val cursor = readableDatabase.query(PRODUCT_TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        lateinit var productDetails: ProductDetails
        if(cursor != null && cursor.moveToFirst()){
            do {
                val productId = cursor.getString(cursor.getColumnIndex(ID))
                val productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))
                val productImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                val productDescription = cursor.getString(cursor.getColumnIndex(DESCRIPTION))
                val productPrice = cursor.getInt(cursor.getColumnIndex(PRICE))
                val productQuantity = cursor.getInt(cursor.getColumnIndex(QUANTITY))
                val subId = cursor.getInt(cursor.getColumnIndex(SUB_ID))
                val catId = cursor.getInt(cursor.getColumnIndex(CAT_ID))
                productDetails = ProductDetails(productId, productName, productImage,
                        productDescription, productPrice, productQuantity, subId, catId)

            }while (cursor.moveToNext())

            cursor.close()
        }
        return productDetails
    }

    fun getUser(userID: String) : User  {
        lateinit var user:User
        val selection = "$ID = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = readableDatabase.query(USER_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                val createdAt = cursor.getString(cursor.getColumnIndex(CREATED_AT))
                val id = cursor.getString(cursor.getColumnIndex(ID))
                val email = cursor.getString(cursor.getColumnIndex(EMAIL))
                val password = cursor.getString(cursor.getColumnIndex(PASSWORD))
                val mobile = cursor.getString(cursor.getColumnIndex(MOBILE))
                val v = cursor.getInt(cursor.getColumnIndex(V))
                user = User(firstName, id, email, password, mobile, createdAt, v)

            } while (cursor.moveToNext())
            cursor.close()
        }
        return user
    }

    fun getAllProductBySubId(subId: Int) : ArrayList<ProductLight> {
        val products = ArrayList<ProductLight>()
        val selection = "$SUB_ID = ?"
        val selectionArgs = arrayOf(subId.toString())
        val columns = arrayOf(ID, PRODUCT_NAME, IMAGE, PRICE, QUANTITY)
        val cursor = readableDatabase.query(PRODUCT_TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        if(cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(ID))
                val productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))
                val productImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                val productPrice = cursor.getInt(cursor.getColumnIndex(PRICE))
                val productQuantity = cursor.getInt(cursor.getColumnIndex(QUANTITY))
                products.add(ProductLight(id, productName, productImage, productPrice, productQuantity))

            }while (cursor.moveToNext())

            cursor.close()
        }
        return products
    }

    fun getAllProductsInUserCart(userID: String)  : ArrayList<ProductLight>  {
        val products = ArrayList<ProductLight>()
        val selection = "$USER_ID = ?"
        val selectionArgs = arrayOf(userID)
        val columns = arrayOf(USER_ID, ITEM_ID, PRICE, QUANTITY, IMAGE, PRODUCT_NAME)
        val cursor = readableDatabase.query(SHOPPING_CART_TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ITEM_ID))
                val productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))
                val productImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                val productPrice = cursor.getInt(cursor.getColumnIndex(PRICE))
                val productQuantity = cursor.getInt(cursor.getColumnIndex(QUANTITY))
                products.add(ProductLight(id, productName, productImage, productPrice, productQuantity))

            } while (cursor.moveToNext())
            cursor.close()
        }
        return products
    }

    private fun getProductInUserCart(userID: String, productId:String) : ProductLight {
        lateinit var product:ProductLight
        val selection = "$USER_ID = ? AND $ITEM_ID = ?"
        val selectionArgs = arrayOf(userID, productId)
        val columns = arrayOf(USER_ID, ITEM_ID, PRICE, QUANTITY, IMAGE, PRODUCT_NAME)
        val cursor = readableDatabase.query(SHOPPING_CART_TABLE_NAME, columns, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(ITEM_ID))
                val productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))
                val productImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                val productPrice = cursor.getInt(cursor.getColumnIndex(PRICE))
                val productQuantity = cursor.getInt(cursor.getColumnIndex(QUANTITY))
                product = ProductLight(id,productName, productImage, productPrice, productQuantity)

            } while (cursor.moveToNext())
            cursor.close()
        }
        return product
    }

    fun getAllOrdersBelongingToUser(userID: String)  : ArrayList<UserOrder>  {
        val orders = ArrayList<UserOrder>()
        val selection = "$USER_ID = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = readableDatabase.query(ORDERS_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val orderId = cursor.getString(cursor.getColumnIndex(ORDER_ID))
                val userId = cursor.getString(cursor.getColumnIndex(USER_ID))
                val emailUsed = cursor.getString(cursor.getColumnIndex(EMAIL_USED))
                val mobileUsed = cursor.getString(cursor.getColumnIndex(MOBILE_USED))
                val name = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                val pinCode = cursor.getInt(cursor.getColumnIndex(PIN_CODE))
                val houseNumber = cursor.getString(cursor.getColumnIndex(HOUSE_NUMBER))
                val streetName = cursor.getString(cursor.getColumnIndex(STREET_NAME))
                val city = cursor.getString(cursor.getColumnIndex(CITY))
                val paymentMode = cursor.getString(cursor.getColumnIndex(PAYMENT_MODE))
                val paymentStatus = cursor.getString(cursor.getColumnIndex(PAYMENT_STATUS))
                val date = cursor.getString(cursor.getColumnIndex(DATE))
                val v = cursor.getInt(cursor.getColumnIndex(V))
                val orderSummary = getOrderSummary(orderId)
                val products = getProductsInOrder(orderId)
                orders.add(
                        UserOrder(orderId, userId, emailUsed, mobileUsed,
                                name, pinCode, houseNumber, streetName, city, paymentMode, paymentStatus,
                                date, v, orderSummary, products
                        )
                )

            } while (cursor.moveToNext())
            cursor.close()
        }
        return orders
    }

    private fun getOrderSummary(orderId: String) : OrderSummary  {
        lateinit var orderSummary:OrderSummary
        val selection = "$ORDER_ID = ?"
        val selectionArgs = arrayOf(orderId)
        val cursor = readableDatabase.query(ORDERS_SUMMARY_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val orderSummaryId = cursor.getString(cursor.getColumnIndex(ORDER_SUMMARY_ID))
                val totalAmount = cursor.getFloat(cursor.getColumnIndex(TOTAL_AMOUNT))
                val ourPrice = cursor.getFloat(cursor.getColumnIndex(OUR_PRICE))
                val discount = cursor.getFloat(cursor.getColumnIndex(DISCOUNT))
                val deliveryCharges = cursor.getFloat(cursor.getColumnIndex(DELIVERY_CHARGES))
                val orderAmount = cursor.getFloat(cursor.getColumnIndex(ORDER_AMOUNT))
                orderSummary = OrderSummary(orderSummaryId, totalAmount, ourPrice, discount, deliveryCharges, orderAmount)

            } while (cursor.moveToNext())
            cursor.close()
        }
        return orderSummary
    }

    private fun getProductsInOrder(orderId: String) : ArrayList<Products> {
        val products: ArrayList<Products>  =  ArrayList()
        val selection = "$ORDER_ID = ?"
        val selectionArgs = arrayOf(orderId)
        val cursor = readableDatabase.query(ORDERED_PRODUCTS_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val productId = cursor.getString(cursor.getColumnIndex(ID))
                val mrp = cursor.getFloat(cursor.getColumnIndex(MRP))
                val price = cursor.getFloat(cursor.getColumnIndex(PRICE))
                val quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY))
                val image = cursor.getString(cursor.getColumnIndex(IMAGE))

                products.add(Products(productId, mrp, price, quantity, image))

            } while (cursor.moveToNext())
            cursor.close()
        }
        return products
    }

    fun getAllAddressWithNameAndPhoneForUser(userID: String) : ArrayList<AddressWithNameAndPhone>{
        val address = ArrayList<AddressWithNameAndPhone>()
        val selection = "$USER_ID = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = readableDatabase.query(ADDRESS_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()) {
            do {
                val uid = cursor.getString(cursor.getColumnIndex(USER_ID))
                val addressId = cursor.getString(cursor.getColumnIndex(ADDRESS_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                val mobile = cursor.getString(cursor.getColumnIndex(MOBILE))
                val city = cursor.getString(cursor.getColumnIndex(CITY))
                val country = cursor.getString(cursor.getColumnIndex(COUNTRY))
                val houseNumber = cursor.getString(cursor.getColumnIndex(HOUSE_NUMBER))
                val pinCode = cursor.getInt(cursor.getColumnIndex(PIN_CODE))
                val streetName = cursor.getString(cursor.getColumnIndex(STREET_NAME))
                val v = cursor.getInt(cursor.getColumnIndex(V))
                val type = cursor.getString(cursor.getColumnIndex(TYPE))
                address.add(AddressWithNameAndPhone(addressId, firstName, mobile, houseNumber, streetName, city, country, type, uid, pinCode, v))

            } while (cursor.moveToNext())
            cursor.close()
        }
        return address
    }

    fun updateProduct(product: Product){
        val contentValues = ContentValues()
        contentValues.put(QUANTITY, product.quantity )
        contentValues.put(DESCRIPTION, product.description )
        contentValues.put(CREATED, product.created)
        contentValues.put(ID, product._id)
        contentValues.put(CAT_ID, product.catId)
        contentValues.put(SUB_ID, product.subId)
        contentValues.put(PRODUCT_NAME, product.productName)
        contentValues.put(IMAGE, product.image)
        contentValues.put(UNIT, product.unit)
        contentValues.put(PRICE, product.price.toFloat())
        contentValues.put(MRP, product.mrp.toFloat())
        contentValues.put(POSITION, product.position)
        contentValues.put(STATUS, product.status)
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(product._id)
        try {
            writableDatabase.update(PRODUCT_TABLE_NAME, contentValues, whereClause, whereArgs)
        }catch (e: SQLiteConstraintException){

        }
    }

    private fun updateQuantity(userId: String, product: ProductLight){
        val contentValues = ContentValues()
        contentValues.put(QUANTITY, product.productQuantity)
        val whereClause = "$USER_ID = ? AND $ITEM_ID = ?"
        val whereArgs = arrayOf(userId, product.productId)
        writableDatabase.update(SHOPPING_CART_TABLE_NAME, contentValues, whereClause, whereArgs)
    }

    fun deleteShoppingCart(userId: String) : LiveData<Boolean> {
        val whereClause = "$USER_ID = ?"
        val whereArgs = arrayOf(userId)
        val index = writableDatabase.delete(SHOPPING_CART_TABLE_NAME, whereClause, whereArgs)
        return MutableLiveData((index >= 0))
    }

    fun deleteProductFromCart(userId: String, productId: String) : LiveData<Boolean> {
        val whereClause = "$USER_ID = ? AND $ITEM_ID = ?"
        val whereArgs = arrayOf(userId, productId)
        val index = writableDatabase.delete(SHOPPING_CART_TABLE_NAME, whereClause, whereArgs)
        return MutableLiveData((index >= 0))
    }

    fun deleteUserAddress(userId: String, addressID: String) : LiveData<Boolean> {
        val whereClause = "$USER_ID = ? AND $ADDRESS_ID = ?"
        val whereArgs = arrayOf(userId, addressID)
        val index = writableDatabase.delete(ADDRESS_TABLE_NAME, whereClause, whereArgs)
        return MutableLiveData((index >= 0))
    }

}