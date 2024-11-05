package com.tracking.myweatherapp.features.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracking.clients.common.ApiResult
import com.tracking.clients.common.ApiResult.Success
import com.tracking.clients.common.Message
import com.tracking.myweatherapp.features.weather.model.WeatherEvent
import com.tracking.myweatherapp.features.weather.model.WeatherEvent.SearchClicked
import com.tracking.myweatherapp.features.weather.model.WeatherState
import com.tracking.myweatherapp.features.weather.model.WeatherUiState
import com.tracking.myweatherapp.features.weather.usecase.WeatherScreenRepository
import com.tracking.myweatherapp.services.location.model.LocationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    val repository: WeatherScreenRepository,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<WeatherUiState> =
        MutableStateFlow<WeatherUiState>(WeatherUiState.Empty)
    val stateFlow = _stateFlow.asStateFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun onEventUpdate(event: WeatherEvent) {
        viewModelScope.launch {
            when (event) {
                is WeatherEvent.UpdateSearchQuery -> updateSearchQuery(event.searchQuery)
                SearchClicked -> handleSearchClickEvent()
            }
        }
    }

    private fun updateSearchQuery(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    private fun handleSearchClickEvent() {
        fetchWeatherData(_searchQuery.value)
        _searchQuery.value = ""
    }

    private fun fetchWeatherData(value: String) {
        _stateFlow.value = WeatherUiState.Loading
        viewModelScope.launch {
            val result = repository.getWeather(city = value)
            when (result) {
                is Success<WeatherState> -> handleSuccessResponse(result.response)
                is ApiResult.Error -> handleErrorResponse(result.message)
            }
        }
    }

    private fun handleSuccessResponse(response: WeatherState) {
        _stateFlow.value = WeatherUiState.State(response)
    }

    private fun handleErrorResponse(message: Message) {
        _stateFlow.value = WeatherUiState.Error
    }

    internal fun updateLocationInfo(info: LocationInfo) {
        _stateFlow.value = WeatherUiState.Loading
        viewModelScope.launch {
            val result = repository.getWeather(info)
            when (result) {
                is Success<WeatherState> -> handleSuccessResponse(result.response)
                is ApiResult.Error -> handleErrorResponse(result.message)
            }
        }
    }

    internal fun locationPermissionDenied() {
        // Check if we can prefetch weather data from saved search query
        checkLastSavedLocation()
    }

    internal fun locationServiceDisabled() {
        // Check if we can prefetch weather data from saved search query
        checkLastSavedLocation()
    }

    internal fun checkLastSavedLocation()  {
        repository.readSearchQuery().onEach { searchQuery ->
            if (searchQuery.isNotEmpty() && stateFlow.value !is WeatherUiState.Loading) {
                fetchWeatherData(searchQuery)
            }
        }.launchIn(viewModelScope)
    }
}