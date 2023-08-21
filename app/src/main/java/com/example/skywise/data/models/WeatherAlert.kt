package com.example.skywise.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skywise.domain.settings.SkywiseSettings
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //Date in Milli Seconds

    var startDate: Long? = null,
    var endDate: Long? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var area: String? = null
) {
    private fun getTime(time: Long): String {
        val dtf =
            DateTimeFormatter.ofPattern("h:mm a")
                .withLocale(Locale(SkywiseSettings.lang))

        return Instant.ofEpochSecond(time / 1000).atZone(ZoneId.systemDefault())
            .format(dtf)
    }

    private fun getDay(time: Long): String {
        val dtf =
            DateTimeFormatter.ofPattern("EEE, MM/dd")
                .withLocale(Locale(SkywiseSettings.lang))

        return Instant.ofEpochSecond(time / 1000).atZone(ZoneId.systemDefault())
            .format(dtf)
    }

    fun getStartDay(): String = startDate?.let { getDay(it) } ?: "N/A"

    fun getEndDay(): String = endDate?.let { getDay(it) } ?: "N/A"
    fun getStart(): String = startDate?.let { getTime(it) } ?: "N/A"
    fun getEnd(): String = endDate?.let { getTime(it) } ?: "N/A"
}