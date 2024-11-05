package com.tracking.clients.weather

import com.tracking.clients.common.ApiResult
import com.tracking.clients.weather.model.response.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeatherClientImplTest {
    private lateinit var weatherClient: WeatherClientImpl

    private val weatherApi: WeatherApi = mockk()

    @Before
    fun setup() {
        weatherClient = WeatherClientImpl(weatherApi)
    }

    @Test
    fun `getWeather returns Success with weather data`() = runBlocking {
        val city = "Albany"
        val weatherResponse = mockk<WeatherResponse>()
        coEvery { weatherApi.getWeather(city) } returns weatherResponse

        val result = weatherClient.getWeather(city)

        assertTrue(result is ApiResult.Success)
        assertEquals(weatherResponse, (result as ApiResult.Success).response)
    }

    @Test
    fun `getWeather returns Error on exception`() = runBlocking {
        val city = "Invalid City"
        coEvery { weatherApi.getWeather(city) } throws Exception("Network error")

        val result = weatherClient.getWeather(city)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.code >= 0)
    }

    @Test
    fun `getWeatherForecast returns Success with weather data`() = runBlocking {
        val latitude = 10.0
        val longitude = 20.0
        val weatherResponse = mockk<WeatherResponse>()
        coEvery { weatherApi.getWeatherByCoordinates(latitude.toString(), longitude.toString()) } returns weatherResponse

        val result = weatherClient.getWeatherForecast(latitude, longitude)

        assertTrue(result is ApiResult.Success)
        assertEquals(weatherResponse, (result as ApiResult.Success).response)
    }

    @Test
    fun `getWeatherForecast returns Error on exception`() = runBlocking {
        val latitude = 10.0
        val longitude = 20.0
        coEvery { weatherApi.getWeatherByCoordinates(latitude.toString(), longitude.toString()) } throws Exception("Network error")

        val result = weatherClient.getWeatherForecast(latitude, longitude)

        assertTrue(result is ApiResult.Error)
        assertTrue((result as ApiResult.Error).message.code >= 0)
    }
}