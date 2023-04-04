package com.example.skywise

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.utils.ConnectionUtils
import com.example.skywise.utils.GeocoderUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        SkywiseSettings.initialize(
            this.applicationContext.getSharedPreferences(
                "settings",
                Context.MODE_PRIVATE
            )
        )
        ConnectionUtils.initialize(this.applicationContext.getSystemService(ConnectivityManager::class.java))
        GeocoderUtil.initialize(Geocoder(this.applicationContext, Locale.getDefault()))
        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}