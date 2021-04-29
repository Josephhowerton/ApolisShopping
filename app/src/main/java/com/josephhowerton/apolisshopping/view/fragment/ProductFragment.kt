package com.josephhowerton.apolisshopping.view.fragment

import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.ProductsAdapter
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentProductBinding
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.app.Config.Companion.BTN
import com.josephhowerton.apolisshopping.app.Config.Companion.MESSAGE
import com.josephhowerton.apolisshopping.app.Config.Companion.TITLE
import com.josephhowerton.apolisshopping.interfaces.AddToCartListener
import com.josephhowerton.apolisshopping.viewmodel.ProductViewModel

class ProductFragment : Fragment(), ProductsAdapter.ProductClickListener, AddToCartListener {
    private val mList: ArrayList<ProductLight> = ArrayList()
    private lateinit var binding:FragmentProductBinding
    private lateinit var mViewModel:ProductViewModel
    private lateinit var mAdapter:ProductsAdapter
    private var subCategoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        mAdapter = ProductsAdapter(mList, this, this)

        arguments?.let {
            subCategoryId = it.getInt(SubCategory.SUB_CATEGORY_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_product, container, false)

        init()

        return binding.root
    }

    private fun init() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = mAdapter
        mViewModel.fetchProducts(subCategoryId).observe(viewLifecycleOwner, {
            if(it){
                initProducts()
            }else{
                alertUser()
            }
        })
    }

    private fun initProducts(){
            mList.clear()
            mList.addAll(mViewModel.getAllProductBySubId(subCategoryId))
            mAdapter.notifyDataSetChanged()
        if(mList.isEmpty()){
            binding.txtViewNoItems.visibility = View.VISIBLE
        }
    }

    override fun onProductClick(product: ProductLight) {
        mViewModel.fetchProductsDetails(product.productId).observe(viewLifecycleOwner, {
            if(it){
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.navigation_details, bundleOf(ProductLight.PRODUCT_KEY to product.productId))
            }else{
                alertUser()
            }
        })
    }

    override fun onAddToCart(productLight: ProductLight) {
        mViewModel.addToCart(productLight)
    }

    private fun alertUser() {
        AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(TITLE)
                .setMessage(MESSAGE)
                .setPositiveButton(
                        BTN,
                ) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    requireActivity().finish()
                }.show()
    }
}