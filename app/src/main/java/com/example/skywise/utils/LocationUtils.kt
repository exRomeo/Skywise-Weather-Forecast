package com.example.skywise.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.skywise.settingsscreen.SkywiseSettings

object LocationUtils {
    const val LOCATION_PERMISSIONS_REQUEST_CODE = 101
    lateinit var locationManager: LocationManager

    fun initialize(locationManager: LocationManager) {
        this.locationManager = locationManager
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                /*60*60*1000*/ 0L, 1000F
            ) {
                SkywiseSettings.setLocation(it)
            }
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                /*60*60*1000*/ 0L, 1F
            ) {
                SkywiseSettings.setLocation(it)
            }
        }
    }


    fun requestLocationPermissions(activity: Activity) {
        try {
            if (!isLocationPermissionGranted(activity)
            ) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), LOCATION_PERMISSIONS_REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            print(e.message)
        }

    }

    fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    fun isLocationPermissionGranted(activity: Activity):Boolean{
        return ContextCompat.checkSelfPermission(
            activity.applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            activity.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


}