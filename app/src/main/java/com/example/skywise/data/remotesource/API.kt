package com.example.skywise.data.remotesource

import com.example.skywise.data.WeatherData
import retrofit2.Response
import retrofit2.http.GET

interface API {
    @GET("")
    suspend fun oneCall(): Response<WeatherData>
}