package com.example.skywise.data.remotesource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val BASE_URL = "https://pro.openweathermap.org"
const val API_KEY = "0700dd38f51000c5455a7693bc132580"
object RetrofitClient {

    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val api: API =
        retrofit.create(
            API::class.java
        )
}