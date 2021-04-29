package com.josephhowerton.apolisshopping.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentAddAddressBinding
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.viewmodel.AddressViewModel

class AddAddressFragment : Fragment(), NetworkErrorListener {
    private val empty = "Empty!"
    private lateinit var mViewModel: AddressViewModel
    private lateinit var mBinding: FragmentAddAddressBinding

    private lateinit var editType: Config.Companion.EDIT_TYPE
    private lateinit var address: AddressWithNameAndPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
        editType = requireArguments().get(Config.EDIT_TYPE_KEY) as Config.Companion.EDIT_TYPE

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false)

        initToolbar()

        if (editType == Config.Companion.EDIT_TYPE.EDIT) {
            address = requireArguments().get(AddressWithNameAndPhone.ADDRESS) as AddressWithNameAndPhone
            populateAddress()
        }

        mBinding.btnSubmit.setOnClickListener {
            if(checkAllFields()){
                saveAddress()
            }
        }

        return mBinding.root
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "My Shopping Cart"
        setHasOptionsMenu(true)
    }

    private fun saveAddress() {
        val personName = mBinding.editTextName.text.toString()
        val phoneNumber = mBinding.editTextNumber.text.toString()
        val houseNumber = mBinding.editHouseNumber.text.toString()
        val streetName = mBinding.editStreetName.text.toString()
        val city = mBinding.editCity.text.toString()
        val country = mBinding.editCountry.text.toString()
        val postalCode = mBinding.editPostalCode.text.toString().toInt()
        val type = getAddressType()

        if(editType == Config.Companion.EDIT_TYPE.ADD){
            postAddress(personName, phoneNumber, postalCode, city, country, streetName, houseNumber, type)
        }
        else if(editType == Config.Companion.EDIT_TYPE.EDIT){
            updateAddress(personName, phoneNumber, postalCode, city, country, streetName, houseNumber, type)
        }
    }

    private fun postAddress(personName: String, phoneNumber: String, postalCode: Int, city: String, country: String, streetName: String,
                            houseNumber: String, type: String){
        mViewModel.saveAddress(personName, phoneNumber, postalCode, city, country, streetName, houseNumber, type, this)
                .observe(viewLifecycleOwner, {
                    if(it){
                        requireActivity().onBackPressed()
                    }
                })
    }

    private fun updateAddress(personName: String, phoneNumber: String, postalCode: Int, city: String, country: String, streetName: String,
                            houseNumber: String, type: String){
        mViewModel.updateAddress(address._id, personName, phoneNumber, postalCode, city, country, streetName, houseNumber, type, this)
                .observe(viewLifecycleOwner, {
                    if(it){
                        requireActivity().onBackPressed()
                    }
                })
    }

    private fun getAddressType(): String{
        when(mBinding.radioGroupAddressType.checkedRadioButtonId){
            R.id.radio_btn_home -> return "Home"
            R.id.radio_btn_office -> return "Office"
            R.id.radio_btn_other -> return "Other"
        }
        return "Home"
    }

    private fun checkAllFields(): Boolean {
        var isValid = true
        if(mBinding.editTextName.text.isEmpty() || mBinding.editTextName.text.toString() == ""){
            mBinding.editTextName.error = empty
            isValid = false
        }

        if(mBinding.editTextNumber.text.isEmpty() || mBinding.editTextNumber.text.toString() == ""){
            mBinding.editTextNumber.error = empty
            isValid = false
        }

        if(mBinding.editHouseNumber.text.isEmpty() || mBinding.editHouseNumber.text.toString() == ""){
            mBinding.editHouseNumber.error = empty
            isValid = false
        }

        if(mBinding.editStreetName.text.isEmpty() || mBinding.editStreetName.text.toString() == ""){
            mBinding.editStreetName.error = empty
            isValid = false
        }

        if(mBinding.editCity.text.isEmpty() || mBinding.editCity.text.toString() == ""){
            mBinding.editCity.error = empty
            isValid = false
        }

        if(mBinding.editPostalCode.text.isEmpty() || mBinding.editPostalCode.text.toString() == ""){
            mBinding.editPostalCode.error = empty
            isValid = false
        }

        if(mBinding.editCountry.text.isEmpty() || mBinding.editCountry.text.toString() == ""){
            mBinding.editCountry.error = empty
            isValid = false
        }

        return isValid
    }


    override fun onNetworkError(message: String) {
        alertUser(message)
    }

    private fun alertUser(message:String) {
        AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(Config.TITLE)
                .setMessage(message)
                .setPositiveButton(
                        Config.BTN,
                ) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> requireActivity().onBackPressed()
        }
        return true
    }

    private fun populateAddress() {
        mBinding.editTextName.setText(address.name)
        mBinding.editTextNumber.setText(address.mobile)
        mBinding.editHouseNumber.setText(address.houseNo)
        mBinding.editStreetName.setText(address.streetName)
        mBinding.editCity.setText(address.city)
        mBinding.editCountry.setText(address.country)
        mBinding.editPostalCode.setText(address.pincode.toString())
    }
}