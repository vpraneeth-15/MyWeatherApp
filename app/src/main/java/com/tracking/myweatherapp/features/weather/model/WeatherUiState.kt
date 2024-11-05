package com.tracking.myweatherapp.features.weather.model

sealed class WeatherUiState() {
    object Loading : WeatherUiState()
    object Error : WeatherUiState()
    data class State(val state: WeatherState) : WeatherUiState()
    object Empty : WeatherUiState()
}