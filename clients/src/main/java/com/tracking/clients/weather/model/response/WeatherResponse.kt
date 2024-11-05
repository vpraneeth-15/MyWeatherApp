package com.tracking.clients.weather.model.response

import com.tracking.clients.weather.model.Clouds
import com.tracking.clients.weather.model.Coord
import com.tracking.clients.weather.model.Main
import com.tracking.clients.weather.model.Sys
import com.tracking.clients.weather.model.Weather
import com.tracking.clients.weather.model.Wind
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)