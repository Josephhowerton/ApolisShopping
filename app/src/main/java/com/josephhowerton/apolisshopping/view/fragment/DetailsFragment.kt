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
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentDetailsBinding
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {
    private lateinit var mBinding: FragmentDetailsBinding
    private lateinit var mViewModel: DetailsViewModel
    private var productId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        productId = arguments?.getString(ProductLight.PRODUCT_KEY)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        init()
        initToolbar()

        return mBinding.root
    }


    private fun init(){
        val productLight = mViewModel.getProductDetails(productId)
        mBinding.name.text = productLight.productName
        mBinding.price.text = productLight.productPrice.toString()
        mBinding.description.text = productLight.productDescription

        Glide.with(mBinding.root)
            .load(Config.getImageUrlWithCategoryId(productLight.productImage))
            .centerCrop()
            .placeholder(R.drawable.ic_image_place_holder)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_no_image)
            .into(mBinding.image)
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
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