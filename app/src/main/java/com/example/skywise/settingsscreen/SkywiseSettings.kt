package com.example.skywise.settingsscreen

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import java.time.LocalDate
import java.util.*

object SkywiseSettings {
    private const val TAG = "Skywise Settings"
    const val LATITUDE = "lat"
    const val LONGITUDE = "lon"
    const val ARABIC = "ar"
    const val ENGLISH = "en"
    const val MINUTELY = "minutely"
    const val HOURLY = "hourly"
    private const val METRIC = "metric"
    private const val STANDARD = "standard"
    private const val IMPERIAL = "imperial"

    lateinit var sharedPreferences: SharedPreferences
        private set
    var lat: Double = 0.0
        private set
    var lon: Double = 0.0
        private set
    var units: String = METRIC
        private set
    var lang: String = Locale.getDefault().language
        private set


    fun initialize(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
        readPrefs()
    }

    fun setLatitude(latitude: Double) {
        this.lat = latitude
        sharedPreferences.edit().putString("lat", lat.toString()).apply()
    }

    fun setLongitude(longitude: Double) {
        this.lon = longitude
        sharedPreferences.edit().putString("lon", lon.toString()).apply()
    }

    fun setUnits(units: String) {
        Log.i(TAG, "setUnits: $units")
        this.units = units
        sharedPreferences.edit().putString("units", units).apply()
    }

    fun setLocation(location: Location) {
        setLatitude(location.latitude)
        setLongitude(location.longitude)
    }

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", false)
    }

    fun doneFirstLaunch() {
        sharedPreferences.edit().putBoolean("isFirstTime", true).apply()
    }

    fun language(): String = Locale.getDefault().language

    fun currentDate(): String {
        return LocalDate.now().toString()
    }

    private fun readPrefs() {
        lat = (sharedPreferences.getString("lat", "0.0"))?.toDouble() ?: 0.0
        lon = (sharedPreferences.getString("lon", "0.0"))?.toDouble() ?: 0.0
        units = sharedPreferences.getString("units", units).toString()
    }

}