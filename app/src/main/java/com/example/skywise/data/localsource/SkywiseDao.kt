package com.example.skywise.data.localsource

import androidx.room.*
import com.example.skywise.alertscreen.WeatherAlert
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface SkywiseDao {
    @Query("SELECT * FROM offline_data Where date = :date")
    suspend fun getOfflineData(date:String): OfflineDataModel?

    @Upsert
    suspend fun addOfflineData(offlineDataModel: OfflineDataModel)

    @Query("SELECT * FROM favorite_locations")
    fun getFavoriteLocations(): Flow<List<FavoriteLocation>>

    @Upsert
    suspend fun addLocation(location: FavoriteLocation)

    @Delete
    suspend fun removeLocation(location: FavoriteLocation)

    @Query("SELECT * FROM weather_alerts")
    fun getAllWeatherAlerts(): Flow<List<WeatherAlert>>

    @Insert
    suspend fun addWeatherAlert(alert: WeatherAlert):Long

    @Query("DELETE FROM weather_alerts WHERE id = :id")
    suspend fun removeAlert(id: Int)

    @Query("SELECT * FROM weather_alerts WHERE id = :id")
    suspend fun getWeatherAlert(id: Int): WeatherAlert
}