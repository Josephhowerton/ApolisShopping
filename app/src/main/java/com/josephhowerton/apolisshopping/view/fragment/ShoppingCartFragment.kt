package com.josephhowerton.apolisshopping.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.ShoppingCartAdapter
import com.josephhowerton.apolisshopping.databinding.FragmentShoppingCartBinding
import com.josephhowerton.apolisshopping.interfaces.AdjustQuantityListener
import com.josephhowerton.apolisshopping.interfaces.EmptyListListener
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.viewmodel.ShoppingCartViewModel
import com.josephhowerton.apolisshopping.viewmodel.SplashViewModel


class ShoppingCartFragment : Fragment(), ShoppingCartAdapter.ShoppingCartListener,
        AdjustQuantityListener, EmptyListListener{
    private val mList: ArrayList<ProductLight>  = ArrayList()
    private lateinit var mAdapter:ShoppingCartAdapter
    private lateinit var mBinding: FragmentShoppingCartBinding
    private lateinit var mViewModel: ShoppingCartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(ShoppingCartViewModel::class.java)
        mList.addAll(mViewModel.getUserShoppingCart())
        mAdapter = ShoppingCartAdapter(mList, this, this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_cart, container, false)

        initToolbar()
        initUserShoppingCart()
        initShoppingCart()

        return mBinding.root
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Shopping Cart"
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            requireActivity().onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUserShoppingCart(){
        mAdapter.notifyDataSetChanged()
    }

    private fun initShoppingCart(){
        mBinding.recyclerView.adapter = mAdapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if(mList.isEmpty()){
            mBinding.txtViewNoItems.visibility = View.VISIBLE
            mBinding.btnCheckOut.visibility = View.GONE
            mBinding.textViewSubTotal.visibility = View.GONE
        }
        else{
            mBinding.txtViewNoItems.visibility = View.GONE
            mBinding.btnCheckOut.visibility = View.VISIBLE
            mBinding.textViewSubTotal.visibility = View.VISIBLE
            mBinding.btnCheckOut.isEnabled = true
        }

        mBinding.btnCheckOut.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_address)
        }
    }

    override fun onShoppingCartListener(product: ProductLight) {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            .navigate(R.id.navigation_details, bundleOf(ProductLight.PRODUCT_KEY to product.productId))
    }

    override fun onAddQuantityClickListener(product: ProductLight, position: Int) {
        val isSuccess = mViewModel.addToUserCart(product, 1)
        if(isSuccess){
            mList.clear()
            mList.addAll(mViewModel.getUserShoppingCart())
            mAdapter.notifyItemChanged(position)
        }
    }

    override fun onSubtractQuantityClickListener(product: ProductLight, position: Int) {
        val isSuccess = mViewModel.addToUserCart(product, -1)
        if(isSuccess){
            mList.clear()
            mList.addAll(mViewModel.getUserShoppingCart())
            mAdapter.notifyItemChanged(position)
        }
    }

    override fun onDeleteIfQuantityIsZero(product: ProductLight, position: Int) {
        mViewModel.deleteToUserCart(product.productId).observe(viewLifecycleOwner, {
            if(it){
                mAdapter.removeItemAtPosition(position)
            }
        })
    }

    override fun onShoppingCartChangedListener(sum: Number) {
        mBinding.textViewSubTotalValue.text = sum.toString()
    }

    override fun onEmptyList() {
        mBinding.txtViewNoItems.visibility = View.VISIBLE
        mBinding.btnCheckOut.visibility = View.GONE
        mBinding.textViewSubTotal.visibility = View.GONE
        mBinding.btnCheckOut.isEnabled = false
    }
}