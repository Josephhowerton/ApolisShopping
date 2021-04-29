package com.josephhowerton.apolisshopping.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mViewModel.initUser()
    }
}