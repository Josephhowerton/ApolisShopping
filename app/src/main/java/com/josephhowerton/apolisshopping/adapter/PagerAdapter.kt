package com.josephhowerton.apolisshopping.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.view.fragment.ProductFragment

class PagerAdapter(fragment:FragmentManager, list:ArrayList<SubcategoryLight>) : FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val mList:ArrayList<SubcategoryLight> = list

    override fun getCount(): Int {
        return mList.size
    }

    override fun getItem(position: Int): Fragment {
        return ProductFragment.newInstance(mList[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mList[position].subcategoryName
    }
}