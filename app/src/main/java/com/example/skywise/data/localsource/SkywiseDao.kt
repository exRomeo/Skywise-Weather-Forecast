package com.example.skywise.data.localsource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
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
}