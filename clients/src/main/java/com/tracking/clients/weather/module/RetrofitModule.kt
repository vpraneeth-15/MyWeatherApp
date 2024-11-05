package com.tracking.clients.weather.module

import com.tracking.clients.weather.WeatherClient
import com.tracking.clients.weather.WeatherClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {
    @Binds
    @Singleton
    abstract fun bindWeatherClient(weatherClientImpl: WeatherClientImpl): WeatherClient
}