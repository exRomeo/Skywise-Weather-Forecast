package com.example.skywise.domain.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide

/*
* https://pro.openweathermap.org/data/2.5/onecall?lat=31.0&lon=29.800&lang=ar&units=metric&exclude=minutely,hourly,daily&appid=0700dd38f51000c5455a7693bc132580
* */

const val IMG_URL = "https://openweathermap.org/img/wn/"



@BindingAdapter("icon", "size")
fun loadImage(view: ImageView, icon: String?, size: String) {
    Glide.with(view).load("$IMG_URL$icon$size.png").into(view)
}

@BindingAdapter("lottie_res_id")
fun setAnimation(view: LottieAnimationView, resID: Int) {
    if (resID > 0)
        view.setAnimation(resID)
}