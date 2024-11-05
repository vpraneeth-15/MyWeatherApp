package com.tracking.myweatherapp.services.location.model

sealed class PermissionStatus {
    object Granted : PermissionStatus()
    object Denied : PermissionStatus()
    object RequestPermission: PermissionStatus()
    object LocationDisabled: PermissionStatus()
}