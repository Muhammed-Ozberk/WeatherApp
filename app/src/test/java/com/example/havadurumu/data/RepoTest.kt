package com.example.havadurumu.data

import com.example.havadurumu.services.ApiService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

class RepoTest {
    private lateinit var apiService: ApiService
    private lateinit var call: Call<WeatherResponse>
    private lateinit var repo: Repo

    @Before
    @Suppress("UNCHECKED_CAST")
    fun setUp() {
        apiService = mock(ApiService::class.java)
        call = mock(Call::class.java) as Call<WeatherResponse>
        repo = Repo(apiService)
    }

    @Test
    fun `successful response returns weather data`() {
        val weather = weatherData()
        `when`(apiService.fetchWeatherData("tr", "istanbul")).thenReturn(call)
        `when`(call.execute()).thenReturn(
            Response.success(WeatherResponse(true, "istanbul", listOf(weather)))
        )

        val result = repo.getWeatherData("istanbul")

        assertEquals(listOf(weather), result)
    }

    @Test
    fun `unsuccessful response returns empty list`() {
        `when`(apiService.fetchWeatherData("tr", "ankara")).thenReturn(call)
        `when`(call.execute()).thenReturn(
            Response.error(401, okhttp3.ResponseBody.create(null, "Unauthorized"))
        )

        assertTrue(repo.getWeatherData("ankara").isEmpty())
    }

    @Test
    fun `network exception returns empty list`() {
        `when`(apiService.fetchWeatherData("tr", "izmir")).thenReturn(call)
        `when`(call.execute()).thenThrow(IOException("Network unavailable"))

        assertTrue(repo.getWeatherData("izmir").isEmpty())
    }

    @Test
    fun `blank city returns empty list without calling service`() {
        assertTrue(repo.getWeatherData(" ").isEmpty())
        verifyNoInteractions(apiService)
    }

    private fun weatherData() = WeatherData(
        date = "20.07.2026",
        day = "Pazartesi",
        icon = "https://example.com/icon.png",
        description = "açık",
        status = "Clear",
        degree = "28.0",
        min = "20.0",
        max = "30.0",
        night = "21.0",
        humidity = "45"
    )
}
