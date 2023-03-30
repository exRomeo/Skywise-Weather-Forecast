package com.example.skywise.settings

import com.example.skywise.METRIC
import java.util.*

class SkywiseSettings private constructor () {

    var lat: Double? = null
    var lon: Double? = null
    var units: String = METRIC
    var language: String = Locale.getDefault().language

    companion object {

        @Volatile
        private var INSTANCE: SkywiseSettings? = null

        fun getInstance(): SkywiseSettings {
            return INSTANCE ?: synchronized(this) {
                val instance = SkywiseSettings()
                INSTANCE = instance
                instance
            }
        }
    }
}