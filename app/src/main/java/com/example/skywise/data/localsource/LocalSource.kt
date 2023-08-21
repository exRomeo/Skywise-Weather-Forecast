package com.example.skywise.data.localsource

import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.OfflineDataModel
import com.example.skywise.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun getOfflineData(): OfflineDataModel

    suspend fun addOfflineData(offlineDataModel: OfflineDataModel)
    fun getFavoriteLocations(): Flow<List<FavoriteLocation>>

    suspend fun addLocation(location: FavoriteLocation)

    suspend fun removeLocation(location: FavoriteLocation)

    fun getAllAlerts(): Flow<List<WeatherAlert>>

    suspend fun addWeatherAlert(alert: WeatherAlert): Long

    suspend fun removeAlertByID(id: Int)

    suspend fun getAlertByID(id: Int): WeatherAlert
}