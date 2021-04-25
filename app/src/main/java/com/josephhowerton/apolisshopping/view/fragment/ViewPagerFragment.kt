package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.PagerAdapter
import com.josephhowerton.apolisshopping.databinding.ViewPagerBinding
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.viewmodel.ViewPagerViewModel

class ViewPagerFragment : Fragment() {
    private val mList:ArrayList<SubcategoryLight> = ArrayList()
    private lateinit var mBinding:ViewPagerBinding
    private lateinit var mViewModel:ViewPagerViewModel
    private lateinit var pagerAdapter: PagerAdapter
    private var catId:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ViewPagerViewModel::class.java)
        pagerAdapter = PagerAdapter(this, mList)

        catId = arguments?.getInt(CategoryLight.CATEGORY_ID)!!
        Log.println(Log.ASSERT, TAG, "category id " + catId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        mBinding = DataBindingUtil.inflate(inflater, R.layout.view_pager, container, false)

        init()

        return mBinding.root
    }

    private val TAG = "ViewPagerFragment"

    private fun init(){
        mBinding.viewPager.adapter = pagerAdapter
        initSubcategory()
        initToolbar()
    }

    private fun initSubcategory(){
        mList.clear()
        mList.addAll(mViewModel.getSubcategoryByCatId(catId))
        Log.println(Log.ASSERT, TAG, "SIIIIIZE " + mList.size)
        pagerAdapter.notifyDataSetChanged()
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab: TabLayout.Tab, i: Int ->

            tab.text = mList[i].subcategoryName
        }.attach()
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            requireActivity().onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}