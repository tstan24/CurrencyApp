package com.myapp.currencyapp.viewmodel.model

/**
 * Created by Girish Sawant on 04/22/19.
 * Declare as Data Class so it reduces boilerplate
 * when comparing items
 */
data class CurrencyRate (
        val currency: String,
        val rate: Float,
        var value : Float
)