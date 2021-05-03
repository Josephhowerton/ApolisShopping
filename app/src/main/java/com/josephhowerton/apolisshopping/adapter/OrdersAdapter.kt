package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.CardViewOrdersBinding
import com.josephhowerton.apolisshopping.interfaces.EmptyListListener
import com.josephhowerton.apolisshopping.model.orders.Order
import com.josephhowerton.apolisshopping.model.orders.UserOrder

class OrdersAdapter(list: ArrayList<UserOrder>, orderListener: OrderListener
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private val mList: ArrayList<UserOrder> = list
    private val mShoppingCartListener = orderListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: CardViewOrdersBinding = DataBindingUtil
            .inflate(LayoutInflater.from(parent.context), R.layout.card_view_orders, parent, false)
        return OrderViewHolder(binding, mShoppingCartListener)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class OrderViewHolder(binding: CardViewOrdersBinding, orderListener: OrderListener)
        : RecyclerView.ViewHolder(binding.root){

        private val mBinding: CardViewOrdersBinding = binding
        private val mOrderListener = orderListener

        fun bind(order: UserOrder){
            mBinding.textViewOrderId.text = order.orderId
            mBinding.textViewOrderName.text = order.date
            mBinding.textViewOrderPaymentMode.text = order.paymentMode
            mBinding.textViewOrderPrice.text = order.orderSummary.orderAmount.toString()
            mBinding.textViewOrderStatus.text = order.paymentStatus

            mBinding.order.setOnClickListener{
                mOrderListener.onOrderClickListener(order)
            }

        }
    }

    interface OrderListener{
        fun onOrderClickListener(order: UserOrder)
    }
}