package com.example.skywise.data.localsource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skywise.data.FavoriteLocation

@Database(entities = [FavoriteLocation::class, OfflineDataModel::class], version = 1, exportSchema = false)
abstract class RoomClient : RoomDatabase() {
    abstract fun getDao(): SkywiseDao

    companion object {
        @Volatile
        var INSTANCE: RoomClient? = null

        fun getInstance(context: Context): RoomClient {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, RoomClient::class.java, "skywise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}