package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.CardViewProductBinding
import com.josephhowerton.apolisshopping.model.orders.Products

class OrderProductsAdapter(list: ArrayList<Products>) : RecyclerView.Adapter<OrderProductsAdapter.OrderViewHolder> (){
    private val mList = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding: CardViewProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
            parent.context),
            R.layout.card_view_product,
            parent,
            false
        )

        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bindCategory(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class OrderViewHolder(binding: CardViewProductBinding) : RecyclerView.ViewHolder(binding.root){
        private val mBinding = binding

        fun bindCategory(product: Products){
            Glide.with(mBinding.root)
                .load(Config.getImageUrlWithCategoryId(product.image))
                .centerCrop()
                .placeholder(R.drawable.ic_image_place_holder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_no_image)
                .into(mBinding.image)
        }
    }
}