                package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.PagerAdapter
import com.josephhowerton.apolisshopping.databinding.ViewPagerBinding
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.viewmodel.ViewPagerViewModel

class ViewPagerFragment : Fragment() {
    private val mList:ArrayList<SubcategoryLight> = ArrayList()
    private lateinit var mBinding:ViewPagerBinding
    private lateinit var mViewModel:ViewPagerViewModel
    private lateinit var pagerAdapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(ViewPagerViewModel::class.java)
        mViewModel.categoryId = arguments?.getInt(CategoryLight.CATEGORY_ID)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        mBinding = DataBindingUtil.inflate(inflater, R.layout.view_pager, container, false)

        init()

        return mBinding.root
    }

    private fun init(){
        pagerAdapter = PagerAdapter(this, mList)
        mBinding.viewPager.adapter = pagerAdapter
        initSubcategory()
        initToolbar()
    }

    private fun initSubcategory(){
        mList.clear()
        mList.addAll(mViewModel.getSubcategoryByCatId(mViewModel.categoryId))
        pagerAdapter.notifyDataSetChanged()
        if(mList.isEmpty()){
            mBinding.txtViewNoItems.visibility = View.VISIBLE
        }else{
            TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab: TabLayout.Tab, i: Int ->
                tab.text = mList[i].subcategoryName
            }.attach()
        }
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home-> requireActivity().onBackPressed()

            R.id.navigation_shopping_cart -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_shopping_cart)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_menu, menu)
    }
}