package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.CategoryAdapter
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentMainBinding
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.viewmodel.MainViewModel
import kotlin.collections.ArrayList

class MainFragment : Fragment(), CategoryAdapter.CategoryClickListener{
    private lateinit var mBinding:FragmentMainBinding
    private lateinit var mViewModel:MainViewModel
    private val mList: ArrayList<CategoryLight> = ArrayList()
    private lateinit var mAdapter:CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container,false)

        init()
        initBanner()
        initCategory()

        return mBinding.root
    }

    private fun init() {
        mAdapter = CategoryAdapter(mList, this)
        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.recycler.adapter = mAdapter
    }

    private fun initBanner(){
        Glide.with(mBinding.root)
            .load(Config.BANNER_IMAGE)
            .centerCrop()
            .placeholder(R.drawable.ic_image_place_holder)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_no_image)
            .into(mBinding.image)
    }

    private fun initCategory(){
            mList.clear()
            mList.addAll(mViewModel.getAllCategory())
            mAdapter.notifyDataSetChanged()
    }


    override fun onCategoryClick(category: CategoryLight) {
        mViewModel.fetchSubCategory(category.categoryId)
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.navigation_sub_category, bundleOf(CategoryLight.CATEGORY_ID to category.categoryId))
    }
}