package com.example.skywise.data.remotesource

import com.example.skywise.data.models.WeatherData
import retrofit2.http.Query

interface RemoteSource {

    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") unit: String,
        @Query("exclude") exclude: String
    ): WeatherData
}