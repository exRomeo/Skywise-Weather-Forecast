package com.example.skywise.data

import android.util.Log
import com.example.skywise.data.localsource.OfflineDataModel
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.utils.DataUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class Repository(
    private val retrofit: RetrofitClient, private val room: RoomClient
) {
    val TAG = "Repository"
    private val dao by lazy { room.getDao() }
    private val api by lazy { retrofit.api }

    suspend fun getOfflineData(): OfflineDataModel {
        return dao.getOfflineData(SkywiseSettings.currentDate()) ?: OfflineDataModel(0.0, 0.0)
    }

    suspend fun addOfflineData(offlineDataModel: OfflineDataModel) {
        dao.addOfflineData(offlineDataModel)
    }

    fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return dao.getFavoriteLocations()
    }

    suspend fun addLocation(location: FavoriteLocation) {
        dao.addLocation(location)
    }

    suspend fun removeLocation(location: FavoriteLocation) {
        dao.removeLocation(location)
    }


    fun getLocationData(
        lat: Double,
        lon: Double,
        language: String,
        units: String,
        exclude: List<String>,
        apiKey: String
    ): Flow<WeatherData> {
        return flow {
            val response = api.oneCall(
                lat, lon, language, units, exclude, apiKey
            )
            Log.i(TAG, "getLocationData: units $units")
            if (response.isSuccessful && response.body() != null) {
                val weatherData = response.body()!!
                saveToDatabase(weatherData)
                emit(weatherData)
            } else throw Exception("Couldn't Fetch Data !")
        }
    }


    private fun saveToDatabase(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            addOfflineData(
                DataUtils.onlineDataToOfflineData(
                    weatherData
                )
            )
        }
    }
}