package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application)

    fun signIn(){

    }

    fun signUp(name: String, email: String, password: String, phone: String) : LiveData<Boolean> {
        return repository.signUp(name, email, password, phone)
    }
}