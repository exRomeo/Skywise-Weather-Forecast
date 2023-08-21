package com.example.skywise.data.repository


import android.util.Log
import com.example.skywise.data.localsource.LocalSource
import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.OfflineDataModel
import com.example.skywise.data.models.WeatherAlert
import com.example.skywise.data.models.WeatherData
import com.example.skywise.data.remotesource.RemoteSource
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.utils.DataUtils
import com.example.skywise.domain.utils.GeocoderUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteSource: RemoteSource, private val localSource: LocalSource
) : Repository {

    private val TAG = "Repository"
    override suspend fun getOfflineData(): OfflineDataModel {
        return localSource.getOfflineData()
    }

    override suspend fun addOfflineData(offlineDataModel: OfflineDataModel) {
        localSource.addOfflineData(offlineDataModel)
    }

    override fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return localSource.getFavoriteLocations()
    }

    override suspend fun addLocation(location: FavoriteLocation) {
        localSource.addLocation(location)
    }

    override suspend fun removeLocation(location: FavoriteLocation) {
        localSource.removeLocation(location)
    }

    override fun getLocationData(
        lat: Double,
        lon: Double,
        language: String,
        units: String,
        exclude: String
    ): Flow<WeatherData> {
        return flow {
            val weatherData = remoteSource.getWeatherData(
                lat = lat,
                lon = lon,
                lang = language,
                unit = units,
                exclude = exclude
            )
            Log.i(TAG, "getLocationData: units $units")
            Log.i(TAG, "getLocationData: units $lat  $lon")
            weatherData.area = GeocoderUtil.getLocationName(lat, lon)
            saveToDatabase(weatherData)
            emit(weatherData)

        }
    }

    override suspend fun getPeriodic(): WeatherData {

        val weatherData = remoteSource.getWeatherData(
            lat = SkywiseSettings.lat,
            lon = SkywiseSettings.lon,
            lang = SkywiseSettings.lang,
            unit = SkywiseSettings.units,
            exclude = "${SkywiseSettings.MINUTELY},${SkywiseSettings.HOURLY}"
        )

        weatherData.area = GeocoderUtil.getLocationName(SkywiseSettings.lat, SkywiseSettings.lon)

        return weatherData
    }

    override fun saveToDatabase(weatherData: WeatherData) {
        CoroutineScope(Dispatchers.IO).launch {
            addOfflineData(
                DataUtils.onlineDataToOfflineData(
                    weatherData
                )
            )
        }
    }

    override fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return this.localSource.getAllAlerts()
    }

    override suspend fun addWeatherAlert(alert: WeatherAlert): Long {
        return this.localSource.addWeatherAlert(alert)
    }

    override suspend fun removeAlert(weatherAlert: WeatherAlert) {
        this.localSource.removeAlertByID(weatherAlert.id)
    }

    override suspend fun removeAlertByID(id: Int) {
        this.localSource.removeAlertByID(id)
    }

    override suspend fun getAlertByID(id: Int): WeatherAlert {
        return localSource.getAlertByID(id)
    }

    /*    suspend fun clearDatabase() {
            localSource.clearOfflineData()
            localSource.clearFavoriteLocations()
            localSource.clearWeatherAlerts()
        }*/
}