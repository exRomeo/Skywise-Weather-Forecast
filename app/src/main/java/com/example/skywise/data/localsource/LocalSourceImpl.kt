package com.example.skywise.data.localsource

import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.data.models.OfflineDataModel
import com.example.skywise.data.models.WeatherAlert
import com.example.skywise.data.roomdb.WeatherDatabase
import com.example.skywise.domain.settings.SkywiseSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSourceImpl @Inject constructor(private val weatherDatabase: WeatherDatabase) :
    LocalSource {

    override suspend fun getOfflineData(): OfflineDataModel {
        return weatherDatabase.getOfflineData(SkywiseSettings.currentDate())
            ?: OfflineDataModel(
                0.0,
                0.0,
                description = "No Connection",
                icon = "0",
                area = "N/A"
            )
    }

    override suspend fun addOfflineData(offlineDataModel: OfflineDataModel) {
        weatherDatabase.addOfflineData(offlineDataModel)
    }

    override fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return weatherDatabase.getFavoriteLocations()
    }

    override suspend fun addLocation(location: FavoriteLocation) {
        weatherDatabase.addLocation(location)
    }

    override suspend fun removeLocation(location: FavoriteLocation) {
        weatherDatabase.removeLocation(location)
    }

    override fun getAllAlerts(): Flow<List<WeatherAlert>> {
        return weatherDatabase.getAllWeatherAlerts()
    }

    override suspend fun addWeatherAlert(alert: WeatherAlert): Long {
        return weatherDatabase.addWeatherAlert(alert)
    }


    override suspend fun removeAlertByID(id: Int) {
        weatherDatabase.removeAlert(id)
    }

    override suspend fun getAlertByID(id: Int): WeatherAlert {
        return weatherDatabase.getWeatherAlert(id)
    }

}