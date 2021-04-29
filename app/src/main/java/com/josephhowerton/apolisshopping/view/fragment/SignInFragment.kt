package com.josephhowerton.apolisshopping.view.fragment

import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.app.Config.Companion.SHARED_PREFERENCE_NAME
import com.josephhowerton.apolisshopping.databinding.FragmentSignInBinding
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener
import com.josephhowerton.apolisshopping.view.activity.MainActivity
import com.josephhowerton.apolisshopping.viewmodel.AuthViewModel

class SignInFragment : Fragment(), NetworkErrorListener {
    private lateinit var mBinding: FragmentSignInBinding
    private lateinit var mViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        init()

        mBinding.textSignUp.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_sign_out)
        }

        return mBinding.root
    }

    private fun init(){
        mBinding.btnSubmit.setOnClickListener {
            if (mBinding.editTextPassword.text.length < 6){
                mBinding.editTextPassword.error = "Invalid Password"
            }else{
                val email = mBinding.editTextEmail.text.toString()
                val password = mBinding.editTextPassword.text.toString()
                signIn(email, password)
            }
        }
    }

    private fun signIn(email: String, password: String){
        mViewModel.signIn(email, password,this).observe(viewLifecycleOwner, {
            if(it){
                goToMain()
            }
        })
    }

    private fun goToMain(){
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        requireActivity().finish()
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
}