package com.example.havadurumu.data

data class WeatherResponse(
    val success: Boolean,
    val city: String,
    val result: List<WeatherData>
)