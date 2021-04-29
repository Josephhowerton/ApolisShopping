package com.josephhowerton.apolisshopping.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config.Companion.BTN
import com.josephhowerton.apolisshopping.app.Config.Companion.MESSAGE
import com.josephhowerton.apolisshopping.app.Config.Companion.TITLE
import com.josephhowerton.apolisshopping.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var mViewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mViewModel = SplashViewModel(application)

        mViewModel.init().observe(this, {
            if (it) isCurrentUser() else alertUser()
        })
    }

    private fun isCurrentUser(){
        if(mViewModel.isUserSignedIn()){
            goToMain()
        }else{
            goToAuth()
        }
    }

    private fun alertUser() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton(
                BTN,
            ) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }.show()
    }
    private fun goToMain() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun goToAuth() {
        val intent = Intent(this@SplashActivity, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}