package com.example.skywise.settingsscreen

import android.app.Activity
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.skywise.utils.GeocoderUtil
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
    const val METRIC = "metric"
    const val STANDARD = "standard"
    const val IMPERIAL = "imperial"
    const val SMALL = ".png"
    const val MEDIUM = "@2x.png"
    const val LARGE = "@4x.png"
    const val NOTIFICATION = "notification"
    const val ALERT = "alert"
    const val GPS = "gps"
    const val MAP = "map"

    var requiresLocation = false

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
    var alertType: String = NOTIFICATION
        private set
    var locationType: String = GPS
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
        Log.i(TAG, "setLocation: ${location.latitude}, ${location.longitude}")
        setLatitude(location.latitude)
        setLongitude(location.longitude)
    }

    fun isFirstLaunch(): Boolean {
        return /*sharedPreferences.getBoolean("isFirstTime", false)*/ true
    }

    fun doneFirstLaunch() {
        sharedPreferences.edit().putBoolean("isFirstTime", true).apply()
    }

    fun setLang(language: String) {
        this.lang = language
        sharedPreferences.edit().putString("language", language).apply()
    }

    fun currentDate(): String {
        return LocalDate.now().toString()
    }

    fun setAlertType(alertType: String) {
        this.alertType = alertType
        sharedPreferences.edit().putString("alert_type", alertType).apply()
    }

    fun setLocationType(locationType: String) {
        sharedPreferences.edit().putString("location_type", locationType).apply()
    }

    private fun readPrefs() {
        lat = (sharedPreferences.getString("lat", "0.0"))?.toDouble() ?: 0.0
        lon = (sharedPreferences.getString("lon", "0.0"))?.toDouble() ?: 0.0
        units = sharedPreferences.getString("units", units).toString()
        lang = sharedPreferences.getString("language", lang).toString()
        alertType = sharedPreferences.getString("alert_type", alertType).toString()
        locationType = sharedPreferences.getString("location_type", locationType).toString()
    }

    @Suppress("DEPRECATION")
    fun updateLocale(activity: Activity) {
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        activity.baseContext.resources.updateConfiguration(
            config, activity.baseContext.resources.displayMetrics
        )

        val layoutDirection =
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        activity.window.decorView.layoutDirection = layoutDirection
        GeocoderUtil.initialize(Geocoder(activity.applicationContext, Locale(lang)))
    }


}