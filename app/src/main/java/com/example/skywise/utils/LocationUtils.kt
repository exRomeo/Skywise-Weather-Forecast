package com.example.skywise.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.skywise.settingsscreen.SkywiseSettings

object LocationUtils {

    private lateinit var locationManager: LocationManager

    fun initialize(locationManager: LocationManager) {
        this.locationManager = locationManager
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            /*216000000L*/ 0L,
            1000F
        ) {
            SkywiseSettings.setLocation(it)
        }
    }


    fun requestLocationPermissions(activity: Activity) {

        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PermissionsUtil.LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }

    }


}