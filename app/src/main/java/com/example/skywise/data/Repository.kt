package com.example.skywise.data

import android.content.SharedPreferences
import android.util.Log
import com.example.skywise.API_KEY
import com.example.skywise.METRIC
import com.example.skywise.MINUTELY
import com.example.skywise.data.localsource.OfflineDataModel
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.utils.DataUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class Repository(
    private val retrofit: RetrofitClient,
    private val room: RoomClient,
    private val sharedPreferences: SharedPreferences
) {
    private val dao by lazy { room.getDao() }
    private val api by lazy { retrofit.api }

    suspend fun getOfflineData(): OfflineDataModel {
        return dao.getOfflineData(LocalDate.now().toString()) ?: OfflineDataModel(0.0,0.0)
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

    fun readSavedLocation(): Flow<WeatherData> {
        val lon = sharedPreferences.getString("lon", "0")
        val lat = sharedPreferences.getString("lat", "0")
        println("shared prefs ->> $lon  $lat")

        return if (lat != null && lon != null) {
            Log.i("TAG", "readSavedLocation: succ")
            getLatestSpecificData(lat.toDouble(), lon.toDouble())
        } else flow { throw Exception("Couldn't Fetch Data !") }
    }

    fun getLatestData(): Flow<WeatherData> {
        return flow {
            val response = api
                .oneCall(
                    31.110456666666668,
                    29.792248333333333,
                    Locale.getDefault().language,
                    METRIC,
                    listOf(MINUTELY),
                    API_KEY
                )
            if (response.isSuccessful && response.body() != null)
                emit(response.body()!!)
            else throw Exception("Couldn't Fetch Data !")
        }
    }

    private fun getLatestSpecificData(lat: Double, lon: Double): Flow<WeatherData> {
        return flow {
            val response =
                api.oneCall(
                    lat,
                    lon,
                    Locale.getDefault().language,
                    METRIC,
                    listOf(MINUTELY),
                    API_KEY
                )
            if (response.isSuccessful && response.body() != null) {
                val weatherData = response.body()!!
                saveToDatabase(weatherData)
                emit(weatherData)
            } else throw Exception("Couldn't Fetch Data !")
        }
    }

    private fun saveToDatabase(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.addOfflineData(
                DataUtils.onlineDataToOfflineData(
                    weatherData
                )
            )
        }
    }


}