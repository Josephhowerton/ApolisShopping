package com.josephhowerton.apolisshopping.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.josephhowerton.apolisshopping.R
import com.josephhowerton.apolisshopping.app.Config
import com.josephhowerton.apolisshopping.view.fragment.AddressFragment
import com.josephhowerton.apolisshopping.view.fragment.OrdersFragment
import com.josephhowerton.apolisshopping.view.fragment.PaymentFragment
import com.josephhowerton.apolisshopping.viewmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), PreferenceManager.OnPreferenceTreeClickListener, SharedPreferences.OnSharedPreferenceChangeListener  {
        private lateinit var mViewModel: SettingsViewModel
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.sharedPreferencesName = Config.SHARED_PREFERENCE_NAME
            preferenceManager.sharedPreferencesMode = Context.MODE_PRIVATE
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            preferenceManager.onPreferenceTreeClickListener = this
            mViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }


        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                Config.CURRENT_USER -> mViewModel.signOut()

                Config.ACTION_ADDRESS -> navigate(AddressFragment())

                Config.ACTION_PAYMENT -> navigate(PaymentFragment())

                Config.ACTION_ORDER -> navigate(OrdersFragment())
            }
            return true
        }

        private fun navigate(fragment: Fragment) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, fragment)
                .commit()
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String?) {
            when (s) {
                Config.CURRENT_USER -> signOut(sharedPreferences.getString(s, null))
            }
        }

        private fun signOut(id:String?){
            if(id == null){
                goToAuth()
            }
        }

        private fun goToAuth() {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    class AddressFragment : PreferenceFragmentCompat(){

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            TODO("Not yet implemented")
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> return backButtonPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun backButtonPressed() : Boolean{
        onBackPressed()
        return true
    }
}