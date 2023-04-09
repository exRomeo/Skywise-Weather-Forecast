package com.example.skywise.settingsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skywise.R
import com.example.skywise.databinding.FragmentSettingsBinding
import com.example.skywise.settingsscreen.SkywiseSettings.ALERT
import com.example.skywise.settingsscreen.SkywiseSettings.ARABIC
import com.example.skywise.settingsscreen.SkywiseSettings.ENGLISH
import com.example.skywise.settingsscreen.SkywiseSettings.GPS
import com.example.skywise.settingsscreen.SkywiseSettings.IMPERIAL
import com.example.skywise.settingsscreen.SkywiseSettings.MAP
import com.example.skywise.settingsscreen.SkywiseSettings.METRIC
import com.example.skywise.settingsscreen.SkywiseSettings.NOTIFICATION
import com.example.skywise.settingsscreen.SkywiseSettings.STANDARD
import com.example.skywise.utils.LocationUtils
import com.example.skywise.utils.NotificationUtils
import com.example.skywise.utils.PermissionsUtil

class SettingsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this.requireActivity(),
        )[SettingsViewModel::class.java]
    }
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_settings, container, false
        )
        binding.lifecycleOwner = this.requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.unitsGroup.check(
            when (SkywiseSettings.units) {
                METRIC -> R.id.metric_radioButton
                STANDARD -> R.id.standard_radioButton
                IMPERIAL -> R.id.imperial_radioButton
                else -> 0
            }
        )

        binding.unitsGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.metric_radioButton -> viewModel.setUnits(METRIC)
                R.id.standard_radioButton -> viewModel.setUnits(STANDARD)
                R.id.imperial_radioButton -> viewModel.setUnits(IMPERIAL)
            }
        }

        binding.languageGroup.check(
            when (SkywiseSettings.lang) {
                ARABIC -> R.id.arabic_radioButton
                ENGLISH -> R.id.english_radioButton
                else -> 0
            }
        )

        binding.languageGroup.setOnCheckedChangeListener { _, button ->
            when (button) {
                R.id.arabic_radioButton -> viewModel.setLanguage(ARABIC, this.requireActivity())
                R.id.english_radioButton -> viewModel.setLanguage(ENGLISH, this.requireActivity())
            }
        }

        binding.locationGroup.check(
            when (SkywiseSettings.locationType) {
                GPS -> R.id.gps_radioButton
                MAP -> R.id.map_radioButton
                else -> 0
            }
        )

        binding.locationGroup.setOnCheckedChangeListener { _, button ->
            binding.mapRadioButton.setOnClickListener { viewModel.showMap(this.requireActivity().supportFragmentManager) }
            when (button) {
                R.id.map_radioButton -> {
                    viewModel.setLocationType(MAP)
                }
                R.id.gps_radioButton -> {
                    LocationUtils.requestLocationPermissions(this.requireActivity())
                    LocationUtils.getCurrentLocation()
                    viewModel.setLocationType(GPS)
                }
            }
        }

        binding.alertsGroup.check(
            when (SkywiseSettings.alertType) {
                NOTIFICATION -> R.id.notification_radioButton
                ALERT -> R.id.alert_radioButton
                else -> 0
            }
        )

        binding.alertsGroup.setOnCheckedChangeListener { _, button ->
            when (button) {
                R.id.notification_radioButton -> {
                    NotificationUtils.requestNotificationsPermission(this.requireActivity())
                    viewModel.setAlertType(NOTIFICATION)
                }
                R.id.alert_radioButton -> {
                    PermissionsUtil.checkOverlayPermission(
                        this.requireActivity(),
                        this.requireContext().applicationContext
                    )
                    viewModel.setAlertType(ALERT)
                }
            }
        }
    }
}