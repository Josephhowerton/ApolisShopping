package com.josephhowerton.apolisshopping.view.fragment.settings

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.adapter.AddressAdapter
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentAddressBinding
import com.josephhowerton.apolisshopping.databinding.FragmentAddressSettingsBinding
import com.josephhowerton.apolisshopping.interfaces.AddressEditListener
import com.josephhowerton.apolisshopping.interfaces.EmptyListListener
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.model.Address
import com.josephhowerton.apolisshopping.model.AddressWithNameAndPhone
import com.josephhowerton.apolisshopping.viewmodel.AddressViewModel

class AddressSettingsFragment : Fragment(), AddressAdapter.AddressClickListener, AddressEditListener,
        NetworkErrorListener, EmptyListListener {
    private lateinit var mAdapter: AddressAdapter
    private lateinit var mBinding: FragmentAddressSettingsBinding
    private lateinit var mViewModel: AddressViewModel
    private val mList: ArrayList<AddressWithNameAndPhone> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address_settings, container, false)

        initAdapter()
        initAddress()

        mBinding.btnAddNew.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, AddAddressSettingsFragment::class.java,
                    bundleOf(Config.EDIT_TYPE_KEY to Config.Companion.EDIT_TYPE.ADD))
                .addToBackStack(null)
                .commit()
        }

        return mBinding.root
    }

    private fun initAdapter() {
        mList.clear()
        mList.addAll(mViewModel.getAddresses())
        mAdapter = AddressAdapter(mList, this, this, this)
    }

    private fun initAddress() {
        if (mList.isEmpty()) {
            mBinding.titleAddNewAddress.visibility = View.VISIBLE
        } else {
            mBinding.titleAddNewAddress.visibility = View.GONE
            mBinding.recyclerView.adapter = mAdapter
            mBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onAddressClick(address: AddressWithNameAndPhone) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, AddAddressSettingsFragment::class.java,
                bundleOf(Address.ADDRESS to address,
                    Config.EDIT_TYPE_KEY to Config.Companion.EDIT_TYPE.EDIT))
            .addToBackStack(null)
            .commit()

    }

    override fun onEditClickListener(address: AddressWithNameAndPhone, position: Int) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, AddAddressSettingsFragment::class.java,
                bundleOf(Address.ADDRESS to address,
                    Config.EDIT_TYPE_KEY to Config.Companion.EDIT_TYPE.EDIT))
            .addToBackStack(null)
            .commit()
    }

    override fun onNetworkError(message: String) {
        alertUser(message)
    }

    override fun onDeleteClickListener(address: AddressWithNameAndPhone, position: Int) {
        mViewModel.deleteAddress(address).observe(viewLifecycleOwner, {
            if (it) {
                mAdapter.removeItemAtPosition(position)
            }
        })
    }

    override fun onEmptyList() {
        mBinding.titleAddNewAddress.visibility = View.VISIBLE
    }

    private fun alertUser(message: String) {
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
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressed()
        }
        return true
    }
}