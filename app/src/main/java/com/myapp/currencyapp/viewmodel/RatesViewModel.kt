package com.myapp.currencyapp.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import com.myapp.currencyapp.api.model.ApiClient
import com.myapp.currencyapp.api.model.LatestRates
import com.myapp.currencyapp.viewmodel.model.CurrencyRate

/**
 * Created by Girish Sawant on 04/22/19.
 * viewmodel for rate updates
 */
class RatesViewModel : ViewModel() {

    private val RATE_UPDATE: Any = Object()

    // Default values
    private var baseCurrency: String = "EUR"
    private var baseValue: Float = 100F

    private val latestRatesObs: Observer<LatestRates>;
    private val currencyRates: MutableLiveData<List<CurrencyRate>> = MutableLiveData()

    /**
     * Default Constructor
     *
     * Setup LatestRates Observer from ApiClient
     *
     */
    init {

        latestRatesObs = Observer {
            if (it != null) updateLatestRates(it)
        }

        ApiClient.getLatestRates().observeForever(latestRatesObs)
    }

    /**
     * Stop observing currencyRates when ViewModel is destroyed
     */
    override fun onCleared() {
        ApiClient.getLatestRates().removeObserver(latestRatesObs)
    }

    /**
     * Converts the latest data to a List<CurrencyRate>
     *
     * @param latestRates Latest Rates received from ApiClient.
     */
    private fun updateLatestRates(latestRates: LatestRates) {

        val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

        synchronized(RATE_UPDATE) {

            newCurrencyRates.add(CurrencyRate(latestRates.base!!, 1.0F, baseValue))

            latestRates.rates?.forEach { (currency, rate) ->
                newCurrencyRates.add(CurrencyRate(currency, rate, rate*baseValue))
            }

            currencyRates.value = newCurrencyRates
        }
    }

    fun getCurrencyRates() : LiveData<List<CurrencyRate>> = currencyRates

    fun refreshRates() {
        ApiClient.refreshLatestRates(baseCurrency);
    }

    fun setNewBase(newBaseCurrency: String, newBaseValue: Float) {

        // Ignore if same
        if (baseCurrency.equals(newBaseCurrency))
            return;

        baseCurrency = newBaseCurrency
        baseValue = newBaseValue
        refreshRates()
    }

    fun setNewBaseValue(value: Float) {
        // Ignore so it doesn't enter an infinite loop
        if (baseValue.equals(value))
            return

        synchronized(RATE_UPDATE) {
            baseValue = value

            val newCurrencyRates: MutableList<CurrencyRate> = mutableListOf<CurrencyRate>()

            currencyRates.value?.forEach { newCurrencyRates.add(CurrencyRate(it.currency,it.rate,it.rate*baseValue))}
            currencyRates.value = newCurrencyRates
        }
    }
}