package com.example.skywise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.skywise.utils.LocationUtils
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.weatherscreen.WeatherFragment
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherFragment: WeatherFragment


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        lifecycleScope.launchWhenStarted {
            LocationUtils.getCurrentLocation(this@MainActivity,lifecycleScope).collectLatest {
                LocationUtils.saveLocationToSharedPrefs(this@MainActivity,it)
                println(
                    "SharedFlow sends his emits ${it.longitude} ${it.latitude}"
                )
                LocationUtils.readSavedLocation(this@MainActivity)
            }
        }
        binding.lifecycleOwner = this
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (savedInstanceState == null) {
            weatherFragment = WeatherFragment()
            fragmentTransaction.add(R.id.fragmentContainerView, weatherFragment, "weather fragment")
        }
        fragmentTransaction.commit()
        Log.i("TAG", "onCreate: " + Locale.getDefault().language)
    }

}