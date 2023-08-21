package com.example.skywise.data.apiservices

import com.example.skywise.data.models.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/onecall")
    suspend fun oneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String,
        @Query("units") unit:String,
        @Query("exclude") exclude:String,
        @Query("appid") apiKey:String
    ): WeatherData
}