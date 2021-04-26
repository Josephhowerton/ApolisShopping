package com.josephhowerton.apolisshopping.database

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.product.Product
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.model.user.User

class DBHelper(application: Application) : SQLiteOpenHelper(application, DB_NAME, null, VERSION){

    companion object {
        const val DB_NAME = "APOLIS_SHOPPING"
        const val VERSION = 5

        const val CATEGORY_TABLE_NAME = "category"
        const val SUBCATEGORY_TABLE_NAME = "subcategory"
        const val PRODUCT_TABLE_NAME = "product"
        const val USER_TABLE_NAME = "user"

        const val V = "__v"
        const val ID = "_id"
        const val CAT_DESCRIPTION = "catDescription"
        const val CAT_ID = "catId"
        const val CAT_IMAGE = "catImage"
        const val CAT_NAME = "catName"
        const val POSITION = "position"
        const val SLUG = "slug"
        const val STATUS = "status"

        const val SUB_DESCRIPTION = "subDescription"
        const val SUB_IMAGE = "subImage"
        const val SUB_NAME = "subName"
        const val SUB_ID = "subId"

        const val QUANTITY = "quantity"
        const val DESCRIPTION = "description"
        const val CREATED = "created"
        const val PRODUCT_NAME = "productName"
        const val IMAGE = "image"
        const val UNIT = "unit"
        const val PRICE = "price"
        const val MRP = "mrp"

        const val FIRST_NAME = "firstName"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val MOBILE = "mobile"
        const val CREATED_AT = "createdAt"
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
                "$MOBILE varchar(15), $V integer, PRIMARY KEY ($ID))"

        db?.execSQL(categoryTable)
        db?.execSQL(subcategoryTable)
        db?.execSQL(productTable)
        db?.execSQL(userTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun isCategoryTableFresh() : Boolean {
        val cursor = readableDatabase.query(CATEGORY_TABLE_NAME, null, null, null, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }

    fun isSubcategoryTableFresh(id: Int) : Boolean {
        val selection = "$CAT_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = readableDatabase.query(SUBCATEGORY_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
        }
    }

    fun isProductTableFresh(id: Int) : Boolean{
        val selection = "$SUB_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = readableDatabase.query(PRODUCT_TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            cursor.close()
            return true
        }else{
            cursor.close()
            return false
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

    fun getProductById(id: String) : ProductDetails{
        val selection = "$ID = ?"
        val selectionArgs = arrayOf(id)
        val columns = arrayOf(ID, PRODUCT_NAME, IMAGE, PRICE, DESCRIPTION, QUANTITY)
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

                productDetails = ProductDetails(productId, productName, productImage,
                        productDescription, productPrice, productQuantity)

            }while (cursor.moveToNext())

            cursor.close()
        }
        return productDetails
    }

    fun getAllProductBySubId(subId: Int) : ArrayList<ProductLight> {
        val products = ArrayList<ProductLight>()
        val selection = "$SUB_ID = ?"
        val selectionArgs = arrayOf(subId.toString())
        val columns = arrayOf(ID, PRODUCT_NAME, IMAGE, PRICE)
        val cursor = readableDatabase.query(PRODUCT_TABLE_NAME, columns, selection, selectionArgs, null, null, null)

        if(cursor != null && cursor.moveToFirst()){
            do {
                val id = cursor.getString(cursor.getColumnIndex(ID))
                val productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME))
                val productImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                val productPrice = cursor.getInt(cursor.getColumnIndex(PRICE))

                products.add(ProductLight(id, productName, productImage, productPrice))

            }while (cursor.moveToNext())

            cursor.close()
        }
        return products
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
        writableDatabase.update(PRODUCT_TABLE_NAME, contentValues, whereClause, whereArgs)
    }

    fun updateUser(user: User){
        val contentValues = ContentValues()
        contentValues.put(FIRST_NAME, user.firstName)
        contentValues.put(ID, user._id)
        contentValues.put(EMAIL, user.email)
        contentValues.put(PASSWORD, user.password)
        contentValues.put(MOBILE, user.mobile)
        contentValues.put(CREATED_AT, user.createdAt)
        contentValues.put(V, user.__v )
        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(user._id)
        writableDatabase.update(PRODUCT_TABLE_NAME, contentValues, whereClause, whereArgs)
    }
}