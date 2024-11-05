package com.tracking.myweatherapp.features.weather.usecase.mapper

import com.tracking.clients.weather.model.response.WeatherResponse
import com.tracking.myweatherapp.features.weather.compose.WeatherScreen
import com.tracking.myweatherapp.features.weather.model.Weather
import com.tracking.myweatherapp.features.weather.model.WeatherState
import com.tracking.myweatherapp.features.weather.model.WeatherUiState
import javax.inject.Inject

class WeatherMapper @Inject constructor() {
    fun from(response: WeatherResponse): WeatherState {
            return WeatherState(
                temperature = response.main.temp,
                feelsLike = response.main.feels_like,
                location = response.name,
                weather = response.weather.map {
                    Weather(
                        main = it.description,
                        iconUrl = "https://openweathermap.org/img/wn/${it.icon}@2x.png"
                    )
                },
                searchQuery = "",
            )
    }
}
