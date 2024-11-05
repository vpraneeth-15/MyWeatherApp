package com.tracking.myweatherapp.services.location.injection

import com.tracking.myweatherapp.services.location.LocationManager
import com.tracking.myweatherapp.services.location.LocationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindLocationPermissionManager(impl: LocationManagerImpl): LocationManager
}