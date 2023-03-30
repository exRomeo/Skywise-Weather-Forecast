package com.example.skywise.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.skywise.*
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.settings.SkywiseSettings
import retrofit2.Response
import java.util.*

class Repository(private val retrofit: RetrofitClient,private val sharedPreferences: SharedPreferences) {
    suspend fun readSavedLocation(): Response<WeatherData>{
        val lon = sharedPreferences.getString("lon","0")
        val lat = sharedPreferences.getString("lat","0")
        println("shared prefs ->> $lon  $lat")

        return if (lat != null && lon != null) {
            getLatestSpecificData(lat.toDouble(),lon.toDouble())
        } else Response.error(null,null)
    }
    suspend fun getLatestData(): Response<WeatherData> {
        return retrofit.api
            .oneCall(31.110456666666668,
                29.792248333333333,
                Locale.getDefault().language,
                METRIC,
                listOf(MINUTELY),
                API_KEY)
    }
    private suspend fun getLatestSpecificData(lat:Double, lon:Double): Response<WeatherData> {


        return retrofit.api.oneCall(lat, lon, Locale.getDefault().language, METRIC, listOf(MINUTELY), API_KEY)
    }
}