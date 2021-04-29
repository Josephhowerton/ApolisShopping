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
import androidx.navigation.Navigation
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.databinding.FragmentPaymentBinding
import com.josephhowerton.apolisshopping.model.product.ProductLight
import com.josephhowerton.apolisshopping.viewmodel.PaymentHistoryViewModel
import kotlinx.android.synthetic.main.layout_order_confirmed.view.*

class PaymentFragment : Fragment() {
    private lateinit var mBinding: FragmentPaymentBinding
    private lateinit var mViewModel: PaymentHistoryViewModel
    private val mList:ArrayList<ProductLight> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(PaymentHistoryViewModel::class.java)
        mList.addAll(mViewModel.getUserShoppingCart())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)

        initData()
        initToolbar()
        initPaymentMethod()

        return mBinding.root
    }


    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Shopping Cart"
        setHasOptionsMenu(true)
    }

    private fun initData(){
        calculateTotal()
    }

    private fun calculateTotal(){
        var sum = 0F
        var total = 0F
        val taxMultiplier = 0.10F
        for(product in mList){
            sum = (sum + (product.productPrice.toFloat() * product.productQuantity))
            total = sum
        }

        val taxes: Float = sum * taxMultiplier
        sum += taxes

        if(sum < 100F){
            mBinding.deliveryAmount.text = "5"
            sum += 5f
        }

        mBinding.orderAmount.text = sum.toString()
        mBinding.taxAmount.text = taxes.toString()
        mBinding.subTotalAmount.text = total.toString()
    }

    private fun initPaymentMethod(){
        mBinding.btnPayOnline.setOnClickListener {
            initPaidScreen()
        }

        mBinding.btnPayCast.setOnClickListener {
            initPaidScreen()
        }
    }

    private fun initPaidScreen(){
        mBinding.btnPayOnline.visibility =  View.GONE
        mBinding.btnPayCast.visibility = View.GONE
        mBinding.orderConfirmed.layout.visibility = View.VISIBLE
        mBinding.orderConfirmed.goHome.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_main)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> requireActivity().onBackPressed()
        }
        return true
    }
}