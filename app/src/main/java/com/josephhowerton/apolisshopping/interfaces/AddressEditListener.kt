package com.josephhowerton.apolisshopping.interfaces

import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone

interface AddressEditListener {

    fun onEditClickListener(address: AddressWithNameAndPhone, position: Int)

    fun onDeleteClickListener(address: AddressWithNameAndPhone, position: Int)
}