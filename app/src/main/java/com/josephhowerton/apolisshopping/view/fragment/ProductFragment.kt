package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.ProductsAdapter
import com.josephhowerton.apolisshopping.databinding.FragmentProductBinding
import com.josephhowerton.apolisshopping.model.product.Product
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.model.subcategory.SubCategory
import com.josephhowerton.apolisshopping.model.subcategory.SubcategoryLight
import com.josephhowerton.apolisshopping.viewmodel.MainViewModel

class ProductFragment : Fragment(), ProductsAdapter.ProductClickListener {
    private val mList: ArrayList<ProductLight> = ArrayList()
    private lateinit var binding:FragmentProductBinding
    private lateinit var mViewModel:MainViewModel
    private lateinit var mAdapter:ProductsAdapter
    private lateinit var subCategory: SubCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mAdapter = ProductsAdapter(mList, this)

        arguments?.let {
            subCategory = it.getSerializable(SubCategory.SUB_CATEGORY_KEY) as SubCategory
        }

        mViewModel.fetchProducts(subCategory.subId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_product, container, false)

        init()

        return binding.root
    }

    private fun init() {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = mAdapter
        initProducts()
    }

    private fun initProducts(){
            mList.clear()
            mList.addAll(mViewModel.getAllProductBySubId(subCategory.subId))
            mAdapter.notifyDataSetChanged()
    }

    override fun onProductClick(product: ProductLight) {
        mViewModel.fetchItemDetails(product.productId)
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_details)
    }

    companion object {
        @JvmStatic
        fun newInstance(subCategory: SubcategoryLight) =
            ProductFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SubcategoryLight.SUB_CATEGORY_KEY, subCategory)
                }
            }
    }
}