package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.CardViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.CardViewCategoryBinding
import com.josephhowerton.apolisshopping.databinding.CardViewProductBinding
import com.josephhowerton.apolisshopping.interfaces.AddToCartListener
import com.josephhowerton.apolisshopping.model.product.Product
import com.josephhowerton.apolisshopping.model.product.ProductLight
import kotlin.collections.ArrayList

class ProductsAdapter(list: ArrayList<ProductLight>, listener: ProductClickListener, addToCartListener: AddToCartListener) : RecyclerView.Adapter<ProductsAdapter.MainViewHolder> (){
    private val mList = list
    private val mListener = listener
    private val mAddToCartListener = addToCartListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding:CardViewProductBinding = DataBindingUtil.inflate(LayoutInflater.from(
            parent.context),
            R.layout.card_view_product,
            parent,
            false
        )

        return MainViewHolder(binding, mListener, mAddToCartListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindCategory(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MainViewHolder(binding: CardViewProductBinding, listener: ProductClickListener, addToCartListener: AddToCartListener) : RecyclerView.ViewHolder(binding.root){
        private val mBinding = binding
        private val mListener = listener
        private val mAddToCartListener = addToCartListener
        fun bindCategory(product: ProductLight){
            mBinding.title.text = product.productName

            Glide.with(mBinding.root)
                .load(Config.getImageUrlWithCategoryId(product.productImage))
                .centerCrop()
                .placeholder(R.drawable.ic_image_place_holder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_no_image)
                .into(mBinding.image)

            mBinding.cardView.setOnClickListener {
                mListener.onProductClick(product)
            }

            mBinding.addToCartBtn.setOnClickListener {
                mAddToCartListener.onAddToCart(product)
            }
        }
    }

    interface ProductClickListener{
        fun onProductClick(product: ProductLight)
    }
}