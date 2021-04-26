package com.josephhowerton.apolisshopping.view.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.databinding.FragmentSignInBinding
import com.josephhowerton.apolisshopping.databinding.FragmentSignUpBinding
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.view.activity.MainActivity
import com.josephhowerton.apolisshopping.viewmodel.AuthViewModel

class SignUpFragment : Fragment(), NetworkErrorListener{
    private lateinit var mBinding: FragmentSignUpBinding
    private lateinit var mViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        init()
        initToolbar()

        return mBinding.root
    }

    private fun init(){
        mBinding.btnSubmit.setOnClickListener {
            if (mBinding.editTextPassword.text.length < 6){
                mBinding.editTextPassword.error = "Password Must Be 6 Characters"
            }else{
                val name = mBinding.editTextName.text.toString()
                val email = mBinding.editTextEmail.text.toString()
                val password = mBinding.editTextPassword.text.toString()
                val phone = mBinding.editTextMobile.text.toString()

                signUp(name, email, password, phone)
            }
        }
    }

    private fun initToolbar(){
        (requireActivity() as AppCompatActivity).setSupportActionBar(mBinding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = ""

        setHasOptionsMenu(true)

    }

    private fun signUp(name: String, email: String, password: String, phone: String){
        mViewModel.signUp(name, email, password, phone, this).observe(viewLifecycleOwner, {
            if(it){
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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
                ) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    requireActivity().onBackPressed()
                }.show()
    }
}