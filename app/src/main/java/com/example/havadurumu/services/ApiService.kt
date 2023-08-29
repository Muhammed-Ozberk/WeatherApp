package com.example.havadurumu.services

import com.example.havadurumu.BuildConfig
import com.example.havadurumu.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("weather/getWeather")
    fun fetchWeatherData(
        @Query("data.lang") lang: String,
        @Query("data.city") city: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.FCM_API_KEY
    ): Call<WeatherResponse>
}
