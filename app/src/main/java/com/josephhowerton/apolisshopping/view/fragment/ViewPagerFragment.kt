package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.PagerAdapter
import com.josephhowerton.apolisshopping.databinding.ViewPagerBinding
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.viewmodel.MainViewModel

class ViewPagerFragment : Fragment() {
    private val mList:ArrayList<SubcategoryLight> = ArrayList()
    private lateinit var mBinding:ViewPagerBinding
    private lateinit var mViewModel:MainViewModel
    private lateinit var pagerAdapter: PagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        pagerAdapter = PagerAdapter(requireActivity().supportFragmentManager, mList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        mBinding = DataBindingUtil.inflate(inflater, R.layout.view_pager, container, false)

        init()

        return mBinding.root
    }

    private fun init(){
        mBinding.viewPager.adapter = pagerAdapter
        mBinding.recyclerTabLayout.setUpWithViewPager(mBinding.viewPager)
        initSubcategory()
        initToolbar()
    }

    private fun initSubcategory(){
//        mViewModel.subCategory.observe(viewLifecycleOwner, {
//            mList.clear()
//            mList.addAll(it)
//            pagerAdapter.notifyDataSetChanged()
//        })
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