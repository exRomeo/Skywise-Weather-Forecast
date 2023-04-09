package com.example.skywise.splashscreen

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.mainactivity.MainActivity
import com.example.skywise.onboarding.OnboardingActivity
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //initializing util classes
        SkywiseSettings.initialize(
            this.applicationContext.getSharedPreferences(
                "settings",
                Context.MODE_PRIVATE
            )
        )
        LocationUtils.initialize(this.applicationContext.getSystemService(LocationManager::class.java))

        if (LocationUtils.isLocationPermissionGranted(this))
            startNavigation()
        else
            LocationUtils.requestLocationPermissions(this)

        SkywiseSettings.updateLocale(this)

        ConnectionUtils.initialize(this.applicationContext.getSystemService(ConnectivityManager::class.java))

        NotificationUtils.initialize(this.applicationContext.getSystemService(NotificationManager::class.java))

        GeocoderUtil.initialize(Geocoder(this.applicationContext, Locale(SkywiseSettings.lang)))
        //loading splash screen animation
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationUtils.LOCATION_PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            LocationUtils.getCurrentLocation()
        else {
            Toast.makeText(this, "need location perms", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        startNavigation()

    }

    fun startNavigation() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            if (SkywiseSettings.isFirstLaunch()) {
                startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            } else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}