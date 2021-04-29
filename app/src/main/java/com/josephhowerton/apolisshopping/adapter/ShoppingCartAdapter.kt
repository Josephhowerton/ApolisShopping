package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.RowItemShoppingCartBinding
import com.josephhowerton.apolisshopping.interfaces.AdjustQuantityListener
import com.josephhowerton.apolisshopping.interfaces.EmptyListListener
import com.josephhowerton.apolisshopping.model.product.ProductDetails
import com.josephhowerton.apolisshopping.model.product.ProductLight

class ShoppingCartAdapter(list: ArrayList<ProductLight>, shoppingCartListener: ShoppingCartListener,
                          adjustQuantityListener: AdjustQuantityListener, emptyListListener: EmptyListListener) : RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder>() {

    private val mList: ArrayList<ProductLight> = list
    private val mShoppingCartListener = shoppingCartListener
    private val mAdjustQuantityListener = adjustQuantityListener
    private val mEmptyListListener = emptyListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        val binding:RowItemShoppingCartBinding = DataBindingUtil
            .inflate(LayoutInflater.from(parent.context), R.layout.row_item_shopping_cart, parent, false)
        return ShoppingCartViewHolder(binding, mShoppingCartListener, mAdjustQuantityListener)
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        calculateTotal()
        holder.bind(mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun removeItemAtPosition(position: Int){
        mList.removeAt(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size);

        if(mList.isEmpty()){
            mEmptyListListener.onEmptyList()
        }
    }

    private fun calculateTotal(){
        var sum: Number = 0
        for(product in mList){
            sum = (sum.toFloat() + (product.productPrice.toFloat() * product.productQuantity))
        }
        mShoppingCartListener.onShoppingCartChangedListener(sum)
    }

    class ShoppingCartViewHolder(binding:RowItemShoppingCartBinding, shoppingCartListener: ShoppingCartListener,
                                 adjustQuantityListener: AdjustQuantityListener) : RecyclerView.ViewHolder(binding.root){

        private val mBinding:RowItemShoppingCartBinding = binding
        private val mShoppingCartListener = shoppingCartListener
        private val mAdjustQuantityListener = adjustQuantityListener

        fun bind(product: ProductLight, position: Int){
            mBinding.titleName.text = product.productName
            mBinding.titlePrice.text = product.productPrice.toString()
            mBinding.textViewQuantityValue.text = product.productQuantity.toString()

            Glide.with(mBinding.root)
                .load(Config.getImageUrlWithCategoryId(product.productImage))
                .centerCrop()
                .placeholder(R.drawable.ic_image_place_holder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_no_image)
                .into(mBinding.productImage)

            mBinding.shoppingCart.setOnClickListener{
                mShoppingCartListener.onShoppingCartListener(product)
            }

            mBinding.btnAdd.setOnClickListener {
                incrementQuantity(product, position)
            }

            mBinding.btnSubtract.setOnClickListener {
                decrementQuantity(product, position)
            }

            mBinding.textViewDelete.setOnClickListener {
                mAdjustQuantityListener.onDeleteIfQuantityIsZero(product, position)
            }
        }

        private fun incrementQuantity(product: ProductLight, position: Int){
            var quantity = mBinding.textViewQuantityValue.text.toString().toInt()
            mBinding.textViewQuantityValue.text = (++quantity).toString()
            mAdjustQuantityListener.onAddQuantityClickListener(product, position)
        }
        private fun decrementQuantity(product: ProductLight, position: Int){
            var quantity = mBinding.textViewQuantityValue.text.toString().toInt()
            quantity = (quantity - 1)
            if(quantity>0){
                mBinding.textViewQuantityValue.text = (quantity).toString()
                mAdjustQuantityListener.onSubtractQuantityClickListener(product, position)
            }else{
                mAdjustQuantityListener.onDeleteIfQuantityIsZero(product, position)
            }
        }
    }

    interface ShoppingCartListener{
        fun onShoppingCartListener(product: ProductLight)
        fun onShoppingCartChangedListener(sum: Number)
    }
}