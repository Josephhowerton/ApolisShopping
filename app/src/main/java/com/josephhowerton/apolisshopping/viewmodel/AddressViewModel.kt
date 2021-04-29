package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.model.user.User

class AddressViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    fun getUser() : User {
        return repository.getUser()
    }

    fun getAddresses() : ArrayList<AddressWithNameAndPhone> {
        return repository.getAllAddressWithNameAndPhoneForUser()
    }

    fun saveAddress(name: String, phone: String, pincode: Int, city: String, country: String, streetName: String,
                    houseNo: String, type: String, listener: NetworkErrorListener) : LiveData<Boolean> {
        return repository.postAddress(name, phone, pincode, city, country, streetName, houseNo, type, listener)
    }

    fun updateAddress(addressId: String, name: String, phone: String, pincode: Int, city: String, country: String, streetName: String,
                    houseNo: String, type: String, listener: NetworkErrorListener) : LiveData<Boolean> {
        return repository.updateAddress(addressId, name, phone, pincode, city, country, streetName, houseNo, type, listener)
    }

    fun deleteAddress(address: AddressWithNameAndPhone) : LiveData<Boolean> {
        return repository.deleteAddress(address._id)
    }
}