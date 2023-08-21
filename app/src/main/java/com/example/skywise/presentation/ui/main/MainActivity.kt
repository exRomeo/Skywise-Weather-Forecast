package com.example.skywise.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.skywise.R
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.domain.location.LocationTracker
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.settings.SkywiseSettings.GPS
import com.example.skywise.domain.utils.LocationUtils
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    @Inject
    lateinit var locationTracker: LocationTracker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LocationUtils.isLocationEnabled() && SkywiseSettings.locationType == GPS)
//            LocationUtils.getCurrentLocation()
            lifecycleScope.launch {
                locationTracker.getCurrentLocation()?.let {
                    SkywiseSettings.setLocation(it)
                }
            }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()
        drawerLayout = binding.drawerLayout
        navView = binding.sideDrawer
        navController = Navigation.findNavController(this, R.id.nav_host_content_main)
        navView.setupWithNavController(navController)
        binding.openDrawerButton.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }
}