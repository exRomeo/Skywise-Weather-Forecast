package com.example.skywise.data

import com.example.skywise.*
import com.example.skywise.data.remotesource.RetrofitClient

class Repository(private val retrofit: RetrofitClient) {
    suspend fun getLatestData(): WeatherData {
        return retrofit.api.oneCall(31.0, 29.800, EN, METRIC, listOf(MINUTELY), API_KEY)
            .body() as WeatherData
    }
}