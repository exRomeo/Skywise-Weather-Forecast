package com.example.skywise.data

import com.example.skywise.API_KEY
import com.example.skywise.AR
import com.example.skywise.METRIC
import com.example.skywise.MINUTELY
import com.example.skywise.data.remotesource.RetrofitClient

class Repository(private val retrofit: RetrofitClient) {
    suspend fun getLatestData(): WeatherData {
        return retrofit.api.oneCall(31.0, 29.800, AR, METRIC, listOf(MINUTELY), API_KEY)
            .body() as WeatherData
    }
}