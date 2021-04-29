package com.josephhowerton.apolisshopping.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.CategoryAdapter
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentMainBinding
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.viewmodel.MainViewModel

class MainFragment : Fragment(), CategoryAdapter.CategoryClickListener{

    private lateinit var mBinding:FragmentMainBinding
    private lateinit var mViewModel:MainViewModel
    private val mList: ArrayList<CategoryLight> = ArrayList()
    private lateinit var mAdapter:CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container,false)

        init()
        initBanner()
        initToolbar()
        initCategory()

        return mBinding.root
    }

    private fun init() {
        mAdapter = CategoryAdapter(mList, this)
        mBinding.content.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.content.recycler.adapter = mAdapter
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.content.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)
    }

    private fun initBanner(){
        Glide.with(mBinding.root)
            .load(Config.BANNER_IMAGE)
            .centerCrop()
            .placeholder(R.drawable.ic_image_place_holder)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_no_image)
            .into(mBinding.content.image)
    }

    private fun initCategory(){
            mList.clear()
            mList.addAll(mViewModel.getAllCategory())
            mAdapter.notifyDataSetChanged()
    }


    override fun onCategoryClick(category: CategoryLight) {
        mViewModel.fetchSubCategory(category.categoryId).observe(viewLifecycleOwner, {
            if(it){
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.navigation_sub_category, bundleOf(CategoryLight.CATEGORY_ID to category.categoryId))
            }else{
                alertUser()
            }
        })
    }

    private fun alertUser() {
        AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(Config.TITLE)
                .setMessage(Config.MESSAGE)
                .setPositiveButton(
                        Config.BTN,
                ) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    requireActivity().onBackPressed()
                }.show()
    }

    private val TAG = "MainFragment"

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_settings -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_settings)

            R.id.navigation_shopping_cart -> Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_shopping_cart)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}