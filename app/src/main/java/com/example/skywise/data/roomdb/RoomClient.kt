package com.example.skywise.data.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.OfflineDataModel
import com.example.skywise.data.models.WeatherAlert

@Database(
    entities = [FavoriteLocation::class,
        OfflineDataModel::class,
        WeatherAlert::class],
    version = 1,
    exportSchema = false
)
abstract class RoomClient : RoomDatabase() {
    abstract fun getWeatherDatabase(): WeatherDatabase

}