package com.example.skywise.data.repository

import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.OfflineDataModel
import com.example.skywise.data.models.WeatherAlert
import com.example.skywise.data.models.WeatherData
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getOfflineData(): OfflineDataModel

    suspend fun addOfflineData(offlineDataModel: OfflineDataModel)

    fun getFavoriteLocations(): Flow<List<FavoriteLocation>>

    suspend fun addLocation(location: FavoriteLocation)

    suspend fun removeLocation(location: FavoriteLocation)

    fun getLocationData(
        lat: Double,
        lon: Double,
        language: String,
        units: String,
        exclude: String
    ): Flow<WeatherData>

    suspend fun getPeriodic(): WeatherData

    fun saveToDatabase(weatherData: WeatherData)

    fun getAllAlerts(): Flow<List<WeatherAlert>>

    suspend fun addWeatherAlert(alert: WeatherAlert): Long

    suspend fun removeAlert(weatherAlert: WeatherAlert)

    suspend fun removeAlertByID(id: Int)

    suspend fun getAlertByID(id: Int): WeatherAlert
}