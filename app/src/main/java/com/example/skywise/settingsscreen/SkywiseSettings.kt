package com.example.skywise.settingsscreen

import android.content.SharedPreferences
import java.util.*

object SkywiseSettings {


    const val AR = "ar"
    const val EN = "en"
    private const val METRIC = "metric"
    const val STANDARD = "standard"
    const val IMPERIAL = "imperial"

    lateinit var sharedPreferences: SharedPreferences
        private set
    var lat: Double? = null
        private set
    var lon: Double? = null
        private set
    var units: String = METRIC
        private set
    var lang: String = Locale.getDefault().language
        private set


    fun initialize(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    fun setLatitude(latitude: Double) {
        lat = latitude
        sharedPreferences.edit().putString("lat", lat.toString()).apply()
    }

    fun setLongitude(longitude: Double) {
        lon = longitude
        sharedPreferences.edit().putString("lon", lon.toString()).apply()
    }

    fun setUnits(units: String) {
        sharedPreferences.edit().putString("units", units).apply()
    }
}