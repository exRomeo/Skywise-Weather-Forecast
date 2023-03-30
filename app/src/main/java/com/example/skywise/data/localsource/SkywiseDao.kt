package com.example.skywise.data.localsource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.skywise.data.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface SkywiseDao {
    @Query("SELECT * FROM product")
    fun getOfflineProducts(): Flow<List<WeatherData>>

    @Query("SELECT * FROM product WHERE isFavorite = 1")
    fun getFavoritesList(): Flow<List<WeatherData>>

    @Upsert
    suspend fun addProduct(product: WeatherData)

    @Delete
    suspend fun deleteProduct(product: WeatherData): Int
}