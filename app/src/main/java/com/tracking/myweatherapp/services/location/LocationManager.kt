package com.tracking.myweatherapp.services.location

import android.app.Activity
import android.content.Context
import com.tracking.myweatherapp.services.location.model.LocationInfo
import com.tracking.myweatherapp.services.location.model.PermissionStatus

interface LocationManager {
    fun requestLocationPermissions(activity: Activity)
    fun checkPermissions(context: Context): PermissionStatus
    fun getCoarseLocation(
        context: Context,
        listener: (LocationInfo?) -> Unit
    )
}
