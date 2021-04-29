package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentDetailsBinding
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.viewmodel.DetailsViewModel
import java.lang.NumberFormatException

class DetailsFragment : Fragment(), TextWatcher {
    private lateinit var mBinding: FragmentDetailsBinding
    private lateinit var mViewModel: DetailsViewModel
    private var productId = ""
    private lateinit var product: ProductDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        productId = arguments?.getString(ProductLight.PRODUCT_KEY)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        init()
        initToolbar()
        initQuantity()

        return mBinding.root
    }


    private fun init(){
        product = mViewModel.getProductDetails(productId)
        mBinding.name.text = product.productName
        mBinding.priceValue.text = product.productPrice.toString()
        mBinding.description.text = product.productDescription

        Glide.with(mBinding.root)
            .load(Config.getImageUrlWithCategoryId(product.productImage))
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

    private fun initQuantity(){
        mBinding.btnAdd.setOnClickListener {
            var quantity = mBinding.textViewQuantityValue.text.toString().toInt()
            if(quantity < product.productQuantity){
                mBinding.textViewQuantityValue.text = (++quantity).toString()
            }
        }

        mBinding.btnSubtract.setOnClickListener {
            var quantity = mBinding.textViewQuantityValue.text.toString().toInt()
            if(quantity > 0){
                mBinding.textViewQuantityValue.text = (--quantity).toString()
            }
        }

        mBinding.addToCartBtn.setOnClickListener {
            val quantity = mBinding.textViewQuantityValue.text.toString()
            if(quantity.toInt() > 0){
                mViewModel.addToUserCart(product, quantity.toInt())
            }
        }

        mBinding.textViewQuantityValue.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {
        try {
            val quantity = s.toString().toInt()
            if(quantity > product.productQuantity){
                mBinding.textViewQuantityValue.text = product.productQuantity.toString()
            }
            else if (quantity < 0){
                mBinding.textViewQuantityValue.text = "0"
            }
        }catch (e:NumberFormatException){
            mBinding.textViewQuantityValue.text = "0"
        }
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