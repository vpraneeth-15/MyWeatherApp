package com.tracking.clients.weather.model

import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    val all: Int
)