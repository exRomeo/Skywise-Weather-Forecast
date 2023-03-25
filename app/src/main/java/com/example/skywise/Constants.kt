package com.example.skywise

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/*
* https://pro.openweathermap.org/data/2.5/onecall?
* lat=33.44
* &lon=-94.04
* &lang=ar
* &units=metric
* &exclude=minutely,hourly,daily
* &appid=0700dd38f51000c5455a7693bc132580
* */
const val BASE_URL = "https://pro.openweathermap.org"
const val AR = "ar"
const val EN = "en"
const val METRIC = "metric"
const val STANDARD = "standard"
const val IMPERIAL = "imperial"
const val MINUTELY = "minutely"
const val HOURLY = "hourly"
const val DAILY = "daily"
const val API_KEY = "0700dd38f51000c5455a7693bc132580"

const val IMG_URL = "https://openweathermap.org/img/wn/"
const val SMALL = ".png"
const val MEDIUM = "@2x.png"
const val LARGE = "@4x.png"


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, icon: String) {
    val imgSize: String =  LARGE
    Glide.with(view).load("$IMG_URL$icon$imgSize").into(view)
}