package com.myapp.currencyapp.api.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.myapp.currencyapp.api.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Girish Sawant on 04/22/19.
 * LiveData to publish it to whoever is listening,
 * in this sample it's going to be the RatesViewModel
 */
object ApiClient {
    private val BASE_URL: String = "https://revolut.duckdns.org/"

    private val service: ApiInterface

    private val latestRates: MutableLiveData<LatestRates> = MutableLiveData()

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(ApiInterface::class.java)
    }

    /**
     * Use LiveData to subscribe the latest data from the server
     */
    fun getLatestRates() : LiveData<LatestRates> = latestRates

    fun refreshLatestRates(baseCurrency : String)  {

        service.getLatestRates(baseCurrency).enqueue(object : Callback<LatestRates> {
            override fun onFailure(call: Call<LatestRates>?, t: Throwable?) {
                //Handle errors - No internet connection, etc
            }

            override fun onResponse(call: Call<LatestRates>, response: Response<LatestRates>) {

                if (response.isSuccessful)
                    latestRates.value = response.body()
            }
        })
    }

}