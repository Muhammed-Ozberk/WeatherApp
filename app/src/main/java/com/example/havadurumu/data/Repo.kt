package com.example.havadurumu.data

import com.example.havadurumu.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repo(
    private val apiService: ApiService = createApiService()
) {
    fun getWeatherData(city: String?): ArrayList<WeatherData> {
        if (city.isNullOrBlank()) return arrayListOf()

        return try {
            val response = apiService.fetchWeatherData("tr", city).execute()
            if (response.isSuccessful) {
                ArrayList(response.body()?.result.orEmpty())
            } else {
                arrayListOf()
            }
        } catch (_: Exception) {
            arrayListOf()
        }
    }

    companion object {
        private fun createApiService(): ApiService = Retrofit.Builder()
            .baseUrl("https://api.collectapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
