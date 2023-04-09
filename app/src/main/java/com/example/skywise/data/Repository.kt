package com.example.skywise.data

import android.util.Log
import com.example.skywise.alertscreen.WeatherAlert
import com.example.skywise.data.localsource.OfflineDataModel
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.API_KEY
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.utils.DataUtils
import com.example.skywise.utils.GeocoderUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class Repository(
    private val retrofit: RetrofitClient, private val room: RoomClient
) {
    private val TAG = "Repository"
    private val dao by lazy { room.getDao() }
    private val api by lazy { retrofit.api }

    suspend fun getOfflineData(): OfflineDataModel {
        return this.dao.getOfflineData(SkywiseSettings.currentDate()) ?: OfflineDataModel(0.0, 0.0)
    }

    suspend fun addOfflineData(offlineDataModel: OfflineDataModel) {
        this.dao.addOfflineData(offlineDataModel)
    }

    fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return this.dao.getFavoriteLocations()
    }

    suspend fun addLocation(location: FavoriteLocation) {
        this.dao.addLocation(location)
    }

    suspend fun removeLocation(location: FavoriteLocation) {
        this.dao.removeLocation(location)
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
            Log.i(TAG, "getLocationData: units $lat  $lon")
            if (response.isSuccessful && response.body() != null) {
                val weatherData = response.body()!!
                weatherData.area = GeocoderUtil.getLocationName(lat, lon)
                saveToDatabase(weatherData)
                emit(weatherData)
            } else throw Exception("Couldn't Fetch Data !")
        }
    }

    suspend fun getPeriodic(): WeatherData {

        val weatherData = api.oneCall(
            SkywiseSettings.lat, SkywiseSettings.lon, SkywiseSettings.lang, SkywiseSettings.units,
            listOf(SkywiseSettings.MINUTELY, SkywiseSettings.HOURLY), API_KEY
        ).body()!!

        weatherData.area = GeocoderUtil.getLocationName(SkywiseSettings.lat, SkywiseSettings.lon)

        return weatherData
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

    fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return this.dao.getAllWeatherAlerts()
    }

    suspend fun addWeatherAlert(alert: WeatherAlert):Long {
        return this.dao.addWeatherAlert(alert)
    }

    suspend fun removeAlert(weatherAlert: WeatherAlert) {
        this.dao.removeAlert(weatherAlert.id)
    }

    suspend fun getAlertByID(id: Int): WeatherAlert {
        return this.dao.getWeatherAlert(id)
    }
}