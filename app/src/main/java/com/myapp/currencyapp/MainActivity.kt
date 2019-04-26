package com.myapp.currencyapp

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import com.myapp.currencyapp.ui.RatesFragment
import com.myapp.currencyapp.viewmodel.RatesViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Init our ViewModel and fetch latest data
        val ratesViewModel: RatesViewModel = ViewModelProviders.of(this).get(RatesViewModel::class.java)
        ratesViewModel.refreshRates()

        displayFragmentRates()
    }

    private fun displayFragmentRates() {

        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()

        fragmentTransaction.replace(R.id.mainFrameLayout, RatesFragment.newInstance())
        fragmentTransaction.commit()
    }
}
