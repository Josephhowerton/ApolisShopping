package com.josephhowerton.apolisshopping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.databinding.RowItemAddressBinding
import com.josephhowerton.apolisshopping.interfaces.AddressEditListener
import com.josephhowerton.apolisshopping.interfaces.EmptyListListener
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.model.user.User

class AddressAdapter(user: User, list: ArrayList<AddressWithNameAndPhone>, clickListener: AddressClickListener,
                     editListener: AddressEditListener, emptyListListener: EmptyListListener) : RecyclerView.Adapter<AddressAdapter.MainViewHolder> (){

    private val mList = list
    private val mUser = user
    private val mClickListener = clickListener
    private val mEditListener = editListener
    private val mEmptyListListener = emptyListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: RowItemAddressBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
            parent.context),
            R.layout.row_item_address,
            parent,
            false
        )

        return MainViewHolder(binding, mClickListener, mEditListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindCategory(mUser.firstName, mList[position], position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun removeItemAtPosition(position: Int){
        mList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mList.size)

        if(mList.isEmpty()){
            mEmptyListListener.onEmptyList()
        }
    }

    class MainViewHolder(binding: RowItemAddressBinding, clickListener: AddressClickListener,  editListener: AddressEditListener) : RecyclerView.ViewHolder(binding.root){
        private val mBinding = binding
        private val mClickListener = clickListener
        private val mEditListener = editListener

        fun bindCategory(userName: String, address: AddressWithNameAndPhone, position: Int){
            mBinding.textViewBillToName.text = userName
            mBinding.textViewAddress.text = formatAddress(address.houseNo, address.streetName)
            mBinding.textViewLocation.text = address.city
            mBinding.textViewPostal.text = address.pincode.toString()

            mBinding.cardView.setOnClickListener {
                mClickListener.onAddressClick(address)
            }

            mBinding.textViewEditAddress.setOnClickListener {
                mEditListener.onEditClickListener(address, position)
            }

            mBinding.textViewDeleteAddress.setOnClickListener {
                mEditListener.onDeleteClickListener(address, position)
            }
        }

        private fun formatAddress(houseNumber: String, streetName: String) : String{
            return "$houseNumber $streetName"
        }
    }

    interface AddressClickListener{
        fun onAddressClick(address: AddressWithNameAndPhone)
    }
}