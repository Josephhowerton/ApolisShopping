package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository
import com.josephhowerton.apolisshopping.interfaces.NetworkErrorListener

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    fun signIn(email: String, password: String, listener:NetworkErrorListener) : LiveData<Boolean> {
        return repository.signIn(email, password, listener)
    }

    fun signUp(name: String, email: String, password: String, phone: String, listener:NetworkErrorListener) : LiveData<Boolean> {
        return repository.signUp(name, email, password, phone, listener)
    }
}