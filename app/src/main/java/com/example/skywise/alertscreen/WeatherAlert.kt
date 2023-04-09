package com.example.skywise.alertscreen

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.skywise.settingsscreen.SkywiseSettings
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "weather_alerts")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //Date in Milli Seconds
//    var startTime: Long? = null,
//    var endTime: Long? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var area: String? = null
) {
    private fun getTime(time: Long): String {
        val dtf =
            DateTimeFormatter.ofPattern("h:mm a yyyy/MM/dd")
                .withLocale(Locale(SkywiseSettings.lang))

        return Instant.ofEpochSecond(time / 1000).atZone(ZoneId.systemDefault())
            .format(dtf)
    }

    fun getStart():String = startDate?.let { getTime(it) } ?: "N/A"
    fun getEnd():String = endDate?.let { getTime(it) } ?: "N/A"
}