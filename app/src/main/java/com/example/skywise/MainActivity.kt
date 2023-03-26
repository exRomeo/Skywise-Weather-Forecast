package com.example.skywise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.weatherscreen.WeatherFragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherFragment: WeatherFragment


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        lifecycleScope.launchWhenStarted {
            getCurrentLocation(this@MainActivity).collectLatest {
                saveLocationToSharedPrefs(this@MainActivity,it)
                println(
                    "SharedFlow sends his emits ${it.longitude} ${it.latitude}"
                )
                readSavedLocation(this@MainActivity)
            }
        }
        binding.lifecycleOwner = this
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (savedInstanceState == null) {
            weatherFragment = WeatherFragment()
            fragmentTransaction.add(R.id.fragmentContainerView, weatherFragment, "weather fragment")
        }
        fragmentTransaction.commit()
    }

    private fun getCurrentLocation(activity: Activity): SharedFlow<Location> {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val sharedFlow = MutableSharedFlow<Location>()
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions(activity)
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            216000000L,
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
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveLocationToSharedPrefs(activity: Activity, location: Location) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("lat", location.latitude.toString())
        editor.putString("lon", location.longitude.toString())
        editor.apply()
        println("saved")
    }
    private fun readSavedLocation(activity: Activity){
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val lon = sharedPreferences.getString("lon","0")
        val lat = sharedPreferences.getString("lat","0")
        println("shared prefs ->> $lon  $lat")
    }
}