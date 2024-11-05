package com.tracking.clients.weather

import com.tracking.clients.common.ApiResult
import com.tracking.clients.weather.model.response.WeatherResponse

interface WeatherClient {
    suspend fun getWeather(city: String): ApiResult<WeatherResponse>
    suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double,
    ): ApiResult<WeatherResponse>
}