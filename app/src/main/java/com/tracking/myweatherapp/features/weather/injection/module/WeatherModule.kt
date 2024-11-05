package com.tracking.myweatherapp.features.weather.injection.module

import com.tracking.clients.weather.WeatherClient
import com.tracking.myweatherapp.data.LocalUserManger
import com.tracking.myweatherapp.features.weather.usecase.WeatherScreenRepository
import com.tracking.myweatherapp.features.weather.usecase.WeatherScreenUseCase
import com.tracking.myweatherapp.features.weather.usecase.mapper.WeatherMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    abstract fun provideUseCase(
        useCase: WeatherScreenUseCase
    ): WeatherScreenRepository
}
