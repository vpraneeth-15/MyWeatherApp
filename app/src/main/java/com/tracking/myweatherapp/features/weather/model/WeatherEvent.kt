package com.tracking.myweatherapp.features.weather.model

sealed class WeatherEvent {
    data class UpdateSearchQuery(val searchQuery: String) : WeatherEvent()
    object SearchClicked : WeatherEvent()
}