package com.josephhowerton.apolisshopping.view.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {
    private val TITLE = "There was an error!"
    private val MESSAGE = "There was an error finding the data from the network. Check your internet connection and try again later."
    private val BTN = "Try Later"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashActivity = SplashViewModel(application)

        splashActivity.init().observe(this, {
            if(it) goToMain() else alertUser()
        })
    }

    private fun alertUser() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton(
                BTN,
            ) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                finish()
            }.show()
    }

    private fun goToMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }
}