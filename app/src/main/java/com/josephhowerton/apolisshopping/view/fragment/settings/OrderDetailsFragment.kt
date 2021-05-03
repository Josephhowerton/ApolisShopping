package com.josephhowerton.apolisshopping.view.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.OrderProductsAdapter
import com.josephhowerton.apolisshopping.databinding.FragmentOrderDetailsBinding
import com.josephhowerton.apolisshopping.model.orders.UserOrder

class OrderDetailsFragment : Fragment() {
    private lateinit var order:UserOrder
    private lateinit var mBinding: FragmentOrderDetailsBinding
    private lateinit var mAdapter: OrderProductsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = requireArguments().get(UserOrder.ORDER) as UserOrder
        mAdapter = OrderProductsAdapter(order.products)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_details, container, false)


        mBinding.editOrderDate.text = order.date
        mBinding.editPersonName.text = order.name
        mBinding.textPaymentMode.text = order.paymentMode
        mBinding.editPaymentStatus.text = order.paymentStatus
        mBinding.editPaymentAmount.text = order.orderSummary.orderAmount.toString()

        mBinding.editHouseNumber.text = order.houseNo
        mBinding.editStreetName.text = order.streetName
        mBinding.editCity.text = "${order.city} ${order.pincode}"

        mBinding.products.adapter = mAdapter
        mBinding.products.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        return mBinding.root
    }
}