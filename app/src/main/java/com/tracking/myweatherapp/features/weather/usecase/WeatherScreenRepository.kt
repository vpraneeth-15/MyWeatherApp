package com.tracking.myweatherapp.features.weather.usecase

import com.tracking.clients.common.ApiResult
import com.tracking.myweatherapp.features.weather.model.WeatherState
import com.tracking.myweatherapp.services.location.model.LocationInfo
import kotlinx.coroutines.flow.Flow

interface WeatherScreenRepository {
    suspend fun getWeather(city: String): ApiResult<WeatherState>
    fun readSearchQuery(): Flow<String>
    suspend fun getWeather(info: LocationInfo): ApiResult<WeatherState>
    suspend fun saveSearchQuery(query: String)
}