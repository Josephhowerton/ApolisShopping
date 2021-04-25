package com.josephhowerton.apolisshopping.adapter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.view.fragment.ProductFragment

class PagerAdapter(fm: Fragment, list:ArrayList<SubcategoryLight>) : FragmentStateAdapter(fm){

    private val mList:ArrayList<SubcategoryLight> = list


    override fun createFragment(position: Int): Fragment {
        val arg = bundleOf(SubcategoryLight.SUB_CATEGORY_KEY to mList[position].subcategoryId)
        val fragment = ProductFragment()
        arg.also { fragment.arguments = it }
        return fragment
    }

    override fun getItemCount(): Int = mList.size
}
