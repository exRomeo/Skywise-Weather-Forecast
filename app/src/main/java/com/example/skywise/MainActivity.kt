package com.example.skywise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.skywise.databinding.ActivityMainBinding
import com.example.skywise.utils.LocationUtils
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        lifecycleScope.launchWhenStarted {
            LocationUtils.getCurrentLocation(this@MainActivity, lifecycleScope).collectLatest {
                LocationUtils.saveLocationToSharedPrefs(this@MainActivity, it)
                LocationUtils.readSavedLocation(this@MainActivity)
            }
        }
        binding.lifecycleOwner = this

        drawerLayout = binding.drawerLayout
        navView = binding.sideDrawer
        navController = Navigation.findNavController(this, R.id.nav_host_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.weatherFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.openDrawerButton.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}