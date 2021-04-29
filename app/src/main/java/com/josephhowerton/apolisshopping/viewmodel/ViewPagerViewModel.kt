package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight

class ViewPagerViewModel(application: Application) : AndroidViewModel(application) {
    var categoryId:Int = 0

    private val repository = Repository(application)

    fun getProductsBySubCategory(id: Int) : ArrayList<ProductLight>{
        return repository.getAllProductBySubId(id)
    }

    fun getSubcategoryByCatId(id: Int) : ArrayList<SubcategoryLight>{
        return repository.getAllSubcategoryByCatId(id)
    }

}