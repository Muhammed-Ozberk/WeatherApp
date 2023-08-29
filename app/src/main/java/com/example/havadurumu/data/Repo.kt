package com.example.havadurumu.data

import android.util.Log
import com.example.havadurumu.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repo {
    fun getWeatherData(city:String?): ArrayList<WeatherData> {
        val list = ArrayList<WeatherData>()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.collectapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        val call = city?.let { apiService.fetchWeatherData("tr", it) }
        // Ağ çağrısını yap
        try {
            val response = call?.execute()
            if (response != null) {
                if (response.isSuccessful) {
                    val weatherDataResponse = response.body() // Bu kısımda servisten dönen verileri al
                    if (weatherDataResponse != null) {
                        val weatherDataList: List<WeatherData>? = weatherDataResponse.result // WeatherResponse içindeki WeatherData listesini al
                        if (weatherDataList != null) {
                            list.addAll(weatherDataList)
                        }
                    }
                } else {
                    Log.e("TAG", "getWeatherDataElse: ${response.errorBody()}" )
                    // Hata durumuyla ilgili işlemler yapabilirsiniz
                    // Örneğin: throw WeatherDataFetchException("Weather data fetch failed")
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "getWeatherData: $e" )
            // Hata durumuyla ilgili işlemler yapabilirsiniz
        }

        return list
    }
}