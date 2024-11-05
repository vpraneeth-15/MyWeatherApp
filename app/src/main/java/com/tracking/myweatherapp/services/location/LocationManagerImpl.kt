package com.tracking.myweatherapp.services.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.tracking.myweatherapp.services.location.model.LocationInfo
import com.tracking.myweatherapp.services.location.model.PermissionStatus
import javax.inject.Inject
import android. location.LocationManager as SystemLocationManager

class LocationManagerImpl @Inject constructor() : LocationManager {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun requestLocationPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun checkPermissions(context: Context): PermissionStatus {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as SystemLocationManager
        if (!locationManager.isProviderEnabled(SystemLocationManager.GPS_PROVIDER)) {
            return PermissionStatus.LocationDisabled
        }
        val permission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return when (permission) {
            PackageManager.PERMISSION_GRANTED -> PermissionStatus.Granted
            PackageManager.PERMISSION_DENIED -> PermissionStatus.Denied
            else -> PermissionStatus.RequestPermission
        }
    }

    override fun getCoarseLocation(
        context: Context,
        listener: (LocationInfo?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        listener(LocationInfo(latitude, longitude))
                    } else {
                        listener(null)
                    }
                }
                .addOnFailureListener { exception: Exception ->
                    listener(null)
                }
        }
    }
}