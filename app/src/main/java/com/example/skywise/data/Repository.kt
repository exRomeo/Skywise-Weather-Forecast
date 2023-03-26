package com.example.skywise.data

import android.app.Activity
import android.content.Context
import com.example.skywise.*
import com.example.skywise.data.remotesource.RetrofitClient
import retrofit2.Response

class Repository(private val retrofit: RetrofitClient,private val activity: Activity) {
    suspend fun readSavedLocation(): Response<WeatherData>{
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val lon = sharedPreferences.getString("lon","0")
        val lat = sharedPreferences.getString("lat","0")
        println("shared prefs ->> $lon  $lat")

        return if (lat != null && lon != null) {
            getLatestSpecificData(lat.toDouble(),lon.toDouble())
        } else Response.error(null,null)
    }
    suspend fun getLatestData(): Response<WeatherData> {
        return retrofit.api.oneCall(31.110456666666668, 29.792248333333333, EN, METRIC, listOf(MINUTELY), API_KEY)
    }
    private suspend fun getLatestSpecificData(lat:Double, lon:Double): Response<WeatherData> {
        return retrofit.api.oneCall(lat, lon, EN, METRIC, listOf(MINUTELY), API_KEY)
    }
}