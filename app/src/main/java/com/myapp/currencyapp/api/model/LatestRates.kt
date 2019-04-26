package com.myapp.currencyapp.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LatestRates {

    @SerializedName("base")
    @Expose
    val base: String? = null
    @SerializedName("date")
    @Expose
    val date: String? = null
    @SerializedName("rates")
    @Expose
    val rates: Map<String, Float>? = null

}