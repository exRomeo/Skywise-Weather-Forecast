package com.example.skywise.data.remotesource

import com.example.skywise.data.apiservices.WeatherApi
import com.example.skywise.data.models.WeatherData
import javax.inject.Inject
import javax.inject.Named

class RemoteSourceImpl @Inject constructor(
    @Named("APIKEY")
    private val apiKey: String,
    private val weatherApi: WeatherApi
) : RemoteSource {
    override suspend fun getWeatherData(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String,
        exclude: String
    ): WeatherData = weatherApi.oneCall(
        apiKey = apiKey,
        lat = lat,
        lon = lon,
        lang = lang,
        unit = unit,
        exclude = exclude
    )
}