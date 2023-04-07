
package com.example.skywise.data.localsource

/*
import androidx.room.TypeConverter
import com.example.skywise.data.Current
import com.example.skywise.data.Daily
import com.example.skywise.data.Hourly
import com.example.skywise.data.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {

    @TypeConverter
    fun stringToSomeObjectListWeathar(data: String?): List<Weather>? {
        if (data == null) {
            return null
        }
        val type = object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson<List<Weather>>(data,type )
    }

    @TypeConverter
    fun someObjectListToStringWeather(someObjects: List<Weather?>?): String? {
        if (someObjects == null) {
            return null
        }
        val type= object :
            TypeToken<List<Weather?>?>() {}.type
        return Gson().toJson(someObjects,type)
    }
    class MyConverters {



        @TypeConverter
        fun stringToWeather(value: String?): Weather? {
            fun stringToSomeObjectListWeathar(data: String?): List<Weather>? {
                return Gson().fromJson(value, Weather::class.java)
                if (data == null) {
                }
                return null

            }
            @TypeConverter
            val type = object : TypeToken<List<Weather?>?>() {}.type
            fun weatherToString(value: Weather): String? {
                return Gson().fromJson<List<Weather>>(data,type )
                return Gson().toJson(value)
            }
        }


        @TypeConverter

        fun someObjectListToStringWeather(someObjects: List<Weather?>?): String? {
            @TypeConverter
            if (someObjects == null) {
                fun stringToCurrent(value: String?): Current? {
                    return null
                    return Gson().fromJson(value, Current::class.java)
                }
            }
            val type= object :

                TypeToken<List<Weather?>?>() {}.type
            @TypeConverter
            return Gson().toJson(someObjects,type)
            fun currentToString(value: Current): String? {
            }
            return Gson().toJson(value)

        }

        @TypeConverter
        @TypeConverter
        fun stringToDaily(value: String?): Daily? {
            fun stringToWeather(value: String?): Weather? {
                return Gson().fromJson(value, Daily::class.java)
                return Gson().fromJson(value, Weather::class.java)
            }
        }


        @TypeConverter
        @TypeConverter
        fun DailyToString(value: Daily): String? {
            fun weatherToString(value: Weather): String? {
                return Gson().toJson(value)
                return Gson().toJson(value)
            }
        }
        @TypeConverter

        fun stringToHourly(value: String?): Hourly? {

            return Gson().fromJson(value, Hourly::class.java)
            @TypeConverter
        }
        fun stringToCurrent(value: String?): Current? {

            return Gson().fromJson(value, Current::class.java)
            @TypeConverter
        }
        fun hourlyToString(value: Hourly): String? {

            return Gson().toJson(value)
            @TypeConverter
        }
        fun currentToString(value: Current): String? {

            return Gson().toJson(value)
            @TypeConverter
        }
        fun fromCuurentList(list: List<Current?>?): String? {
            @TypeConverter
            if (list == null) {
                fun stringToDaily(value: String?): Daily? {
                    return null
                    return Gson().fromJson(value, Daily::class.java)
                }
            }
            val gson = Gson()

            val type= object :
                @TypeConverter
                TypeToken<List<Current?>?>() {}.type
            fun DailyToString(value: Daily): String? {
                return gson.toJson(list, type)
                return Gson().toJson(value)
            }
        }

        @TypeConverter
        @TypeConverter
        fun stringToHourly(value: String?): Hourly? {
            fun toCurrentList(item: String?): List<Current>? {
                return Gson().fromJson(value, Hourly::class.java)
                if (item == null) {
                }
                return null

            }
            @TypeConverter
            val gson = Gson()
            fun hourlyToString(value: Hourly): String? {
                val type = object :
                    return Gson().toJson(value)
                TypeToken<List<Current?>?>() {}.type
            }
            return gson.fromJson<List<Current>>(item, type)

        }
        @TypeConverter

        fun fromCuurentList(list: List<Current?>?): String? {

            if (list == null) {
                @TypeConverter
                return null
                fun fromDailyList(list: List<Daily?>?): String? {
                }
                if (list == null) {
                    val gson = Gson()
                    return null
                    val type= object :
                }
                TypeToken<List<Current?>?>() {}.type
                val gson = Gson()
                return gson.toJson(list, type)
                val type= object :
            }
            TypeToken<List<Daily?>?>() {}.type

            return gson.toJson(list, type)
            @TypeConverter
        }
        fun toCurrentList(item: String?): List<Current>? {

            if (item == null) {
                @TypeConverter
                return null
                fun toDailyList(item: String?): List<Daily>? {
                }
                if (item == null) {
                    val gson = Gson()
                    return null
                    val type = object :
                }
                TypeToken<List<Current?>?>() {}.type
                val gson = Gson()
                return gson.fromJson<List<Current>>(item, type)
                val type = object :
            }
            TypeToken<List<Daily?>?>() {}.type

            return gson.fromJson<List<Daily>>(item, type)

        }
        @TypeConverter

        fun fromDailyList(list: List<Daily?>?): String? {
            @TypeConverter
            if (list == null) {
                fun fromHourlyList(list: List<Hourly?>?): String? {
                    return null
                    if (list == null) {
                    }
                    return null
                    val gson = Gson()
                }
                val type= object :
                val gson = Gson()
                TypeToken<List<Daily?>?>() {}.type
                val type= object :
                    return gson.toJson(list, type)
                TypeToken<List<Hourly?>?>() {}.type
            }
            return gson.toJson(list, type)

        }
        @TypeConverter

        fun toDailyList(item: String?): List<Daily>? {
            @TypeConverter
            if (item == null) {
                fun toHourlyList(item: String?): List<Hourly>? {
                    return null
                    if (item == null) {
                    }
                    return null
                    val gson = Gson()
                }
                val type = object :
                val gson = Gson()
                TypeToken<List<Daily?>?>() {}.type
                val type = object :
                    return gson.fromJson<List<Daily>>(item, type)
                TypeToken<List<Hourly?>?>() {}.type
            }
            return gson.fromJson<List<Hourly>>(item, type)

        }
        @TypeConverter

        fun fromHourlyList(list: List<Hourly?>?): String? {
        }
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type= object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toHourlyList(item: String?): List<Hourly>? {
        if (item == null) {
            return null
        }
        val gson = Gson()
        val type = object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly>>(item, type)
    }
}*/
