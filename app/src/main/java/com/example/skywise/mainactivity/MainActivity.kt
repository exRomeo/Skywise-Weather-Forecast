package com.example.skywise.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.skywise.R
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.settingsscreen.SkywiseSettings.GPS
import com.example.skywise.utils.LocationUtils
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LocationUtils.isLocationEnabled() && SkywiseSettings.locationType == GPS)
            LocationUtils.getCurrentLocation()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()
        drawerLayout = binding.drawerLayout
        navView = binding.sideDrawer
        navController = Navigation.findNavController(this, R.id.nav_host_content_main)
        navView.setupWithNavController(navController)
        binding.openDrawerButton.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }
}