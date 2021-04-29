package com.josephhowerton.apolisshopping.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.josephhowerton.apolisshopping.Repository

class SettingsViewModel(application: Application) : AndroidViewModel(application){

    private val repository = Repository(application)


    fun signOut(){
        repository.signUserOut()
    }
}
