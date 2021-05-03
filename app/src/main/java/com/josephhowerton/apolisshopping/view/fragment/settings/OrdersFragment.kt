package com.josephhowerton.apolisshopping.view.fragment.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.OrdersAdapter
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.database.DBHelper
import com.josephhowerton.apolisshopping.databinding.FragmentOrdersBinding
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.orders.Order
import com.josephhowerton.apolisshopping.model.orders.UserOrder
import com.josephhowerton.apolisshopping.model.user.User
import com.josephhowerton.apolisshopping.viewmodel.OrdersViewModel
import com.josephhowerton.apolisshopping.viewmodel.PaymentViewModel
import kotlinx.android.synthetic.main.card_view_orders.*

class OrdersFragment : Fragment(), OrdersAdapter.OrderListener {
    private lateinit var mBinding: FragmentOrdersBinding
    private lateinit var mViewModel: OrdersViewModel
    private val orders = ArrayList<UserOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
        orders.clear()
        orders.addAll(mViewModel.getAllOrders())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding =  DataBindingUtil.inflate(layoutInflater, R.layout.fragment_orders, container, false)

        val adapter = OrdersAdapter(orders, this)

        if(orders.isEmpty()){
            mBinding.txtViewNoItems.visibility = View.VISIBLE
        }else{
            mBinding.txtViewNoItems.visibility = View.GONE
            mBinding.orders.adapter = adapter
            mBinding.orders.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }


        return mBinding.root
    }

    override fun onOrderClickListener(order: UserOrder) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, OrderDetailsFragment::class.java,
                bundleOf(UserOrder.ORDER to order)
            )
            .addToBackStack(null)
            .commit()
    }
}