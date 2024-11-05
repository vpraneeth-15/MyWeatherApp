package com.tracking.myweatherapp.features.weather.usecase

import com.tracking.clients.common.ApiResult
import com.tracking.clients.weather.WeatherClient
import com.tracking.myweatherapp.data.LocalUserManger
import com.tracking.myweatherapp.features.weather.model.WeatherState
import com.tracking.myweatherapp.features.weather.usecase.mapper.WeatherMapper
import com.tracking.myweatherapp.services.location.model.LocationInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherScreenUseCase @Inject constructor(
    private val client: WeatherClient,
    private val mapper: WeatherMapper,
    private val localUserManger: LocalUserManger
): WeatherScreenRepository {

    override suspend fun getWeather(city: String): ApiResult<WeatherState> {
        val result = client.getWeather(city)
        return when (result) {
            is ApiResult.Success -> {
                saveSearchQuery(city)
                ApiResult.Success(
                    response = mapper.from(result.response)
                )
            }

            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override fun readSearchQuery(): Flow<String> {
        return localUserManger.readSearchQuery()
    }

    override suspend fun getWeather(info: LocationInfo): ApiResult<WeatherState> {
        val result = client.getWeatherForecast(info.latitude, info.longitude)
        return when (result) {
            is ApiResult.Success -> {
                saveSearchQuery(result.response.name)
                ApiResult.Success(
                    response = mapper.from(result.response)
                )
            }

            is ApiResult.Error -> ApiResult.Error(result.message)
        }
    }

    override suspend fun saveSearchQuery(query: String) {
        localUserManger.saveSearchQuery(query)
    }
}