package com.tracking.myweatherapp.features.weather.model

data class WeatherState(
    val temperature: Double,
    val feelsLike: Double,
    val location: String,
    val weather: List<Weather>,
    val searchQuery: String
)

data class Weather(
    val main: String,
    val iconUrl: String,
)

