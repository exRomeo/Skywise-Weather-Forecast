package com.example.skywise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.weatherscreen.WeatherFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var weatherFragment:WeatherFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (savedInstanceState == null){
            weatherFragment = WeatherFragment()
            fragmentTransaction.add(R.id.fragmentContainerView,weatherFragment,"weather fragment")
        }
        fragmentTransaction.commit()
    }


}