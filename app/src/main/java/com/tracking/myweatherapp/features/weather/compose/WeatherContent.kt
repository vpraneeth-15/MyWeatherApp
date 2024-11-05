package com.tracking.myweatherapp.features.weather.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.tracking.myweatherapp.features.weather.WeatherViewModel

@Composable
fun WeatherContent(
    viewModel: WeatherViewModel = hiltViewModel(),
    onRequestPermissions: () -> Unit,
) {

    val state by viewModel.stateFlow.collectAsState()
    val query: String by viewModel.searchQuery.collectAsState()
    LaunchedEffect(Unit) {
        onRequestPermissions()
    }
    WeatherScreen(
        state = state,
        query = query,
        onEvent = { viewModel.onEventUpdate(it) }
    )
}