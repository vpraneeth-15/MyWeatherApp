package com.tracking.clients.weather

import com.tracking.clients.common.ApiResult
import com.tracking.clients.common.Message
import com.tracking.clients.weather.model.response.WeatherResponse
import javax.inject.Inject

class WeatherClientImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherClient {

    override suspend fun getWeather(city: String): ApiResult<WeatherResponse> {
        return try {
            val response = weatherApi.getWeather(query = city)
            return ApiResult.Success(response)
        } catch (e: Exception) {
            return ApiResult.Error(
                message = Message(
                    code = 500,
                    message = "Something went wrong, please try again"
                )
            )
        }
    }

    override suspend fun getWeatherForecast(
        latitude: Double,
        longitude: Double,
    ): ApiResult<WeatherResponse> {
        return try {
            val response = weatherApi.getWeatherByCoordinates(
                latitude = latitude.toString(),
                longitude = longitude.toString(),
            )
            return ApiResult.Success(response)
        } catch (e: Exception) {
            return ApiResult.Error(
                message = Message(
                    code = 500,
                    message = "Something went wrong, please try again"
                )
            )
        }
    }
}
