package com.tracking.myweatherapp.features.mainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tracking.myweatherapp.features.weather.WeatherViewModel
import com.tracking.myweatherapp.features.weather.compose.WeatherContent
import com.tracking.myweatherapp.services.location.LocationManager
import com.tracking.myweatherapp.services.location.model.PermissionStatus
import com.tracking.myweatherapp.ui.theme.MyWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var locationManager: LocationManager

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    WeatherContent(
                        onRequestPermissions = ::requestLocationPermissions,
                    )
                }
            }
        }
    }

    private fun requestLocationPermissions() {
        val status= locationManager.checkPermissions(this)
        Log.d("MainActivity", "Check Permissions: $status")
        when (status) {
            PermissionStatus.Granted -> {
                locationManager.getCoarseLocation(this) { info ->
                    info?.let {
                        viewModel.updateLocationInfo(info)
                    }
                }
            }
            PermissionStatus.Denied -> {
                viewModel.locationPermissionDenied()
            }
            PermissionStatus.LocationDisabled -> {
                viewModel.locationServiceDisabled()
            }
            PermissionStatus.RequestPermission -> {
                Log.d("MainActivity", "Check Permissions: true")
                locationManager.requestLocationPermissions(this)
                locationManager.getCoarseLocation(this) { info ->
                    info?.let {
                        viewModel.updateLocationInfo(info)
                    }
                }
            }
        }
    }
}
