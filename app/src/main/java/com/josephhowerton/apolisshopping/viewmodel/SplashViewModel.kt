package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.josephhowerton.apolisshopping.Repository

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)

    fun init() : LiveData<Boolean> {
        return repository.init()
    }

    fun initUser(){
        repository.initUser()
    }

    fun isUserSignedIn() : Boolean {
        return repository.isUserSignedIn()
    }

}