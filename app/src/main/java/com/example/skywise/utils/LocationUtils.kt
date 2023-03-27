package com.example.skywise.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object LocationUtils {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        activity: Activity,
        lifecycleScope: LifecycleCoroutineScope
    ): SharedFlow<Location> {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val sharedFlow = MutableSharedFlow<Location>()

        requestLocationPermissions(activity)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            /*216000000L*/ 0L,
            1000F
        ) {
            lifecycleScope.launchWhenStarted { sharedFlow.emit(it) }
            println("first \n${it.longitude}  ${it.latitude}")
        }
        return sharedFlow
    }

    private fun requestLocationPermissions(activity: Activity) {
        try {
            if (ContextCompat.checkSelfPermission(
                    activity.applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION/*,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION*/
                    ),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveLocationToSharedPrefs(activity: Activity, location: Location) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lat", location.latitude.toString())
        editor.putString("lon", location.longitude.toString())
        editor.apply()
        println("saved")
    }

    fun readSavedLocation(activity: Activity) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val lon = sharedPreferences.getString("lon", "0")
        val lat = sharedPreferences.getString("lat", "0")
        println("shared prefs ->> $lon  $lat")
    }

    fun getCityByLocation(location: Location): String {
        return String()
    }
}