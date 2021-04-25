package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.CardViewCategoryBinding
import com.josephhowerton.apolisshopping.model.category.Category
import com.josephhowerton.apolisshopping.model.category.CategoryLight
import kotlin.collections.ArrayList

class CategoryAdapter(list: ArrayList<CategoryLight>, listener: CategoryClickListener) : RecyclerView.Adapter<CategoryAdapter.MainViewHolder> (){
    private val mList = list
    private val mListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding:CardViewCategoryBinding = DataBindingUtil.inflate(LayoutInflater.from(
            parent.context),
            R.layout.card_view_category,
            parent,
            false
        )

        return MainViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindCategory(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class MainViewHolder(binding: CardViewCategoryBinding, listener: CategoryClickListener) : RecyclerView.ViewHolder(binding.root){
        private val mBinding = binding
        private val mListener = listener
        fun bindCategory(category: CategoryLight){
            mBinding.title.text = category.categoryName

            Glide.with(mBinding.root)
                .load(Config.getImageUrlWithCategoryId(category.categoryImage))
                .centerCrop()
                .placeholder(R.drawable.ic_image_place_holder)
                .error(R.drawable.ic_broken_image)
                .fallback(R.drawable.ic_no_image)
                .into(mBinding.image)

            mBinding.cardView.setOnClickListener {
                mListener.onCategoryClick(category)
            }
        }
    }

    interface CategoryClickListener{
        fun onCategoryClick(category: CategoryLight)
    }
}