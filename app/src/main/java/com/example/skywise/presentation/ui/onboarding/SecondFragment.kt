package com.example.skywise.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.skywise.R
import com.example.skywise.databinding.FragmentSecondBinding
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.settings.SkywiseSettings.GPS
import com.example.skywise.domain.settings.SkywiseSettings.MAP
import com.example.skywise.domain.utils.LocationUtils
import com.example.skywise.presentation.ui.mapsheet.MapSheet


class SecondFragment : Fragment() {
    lateinit var binding: FragmentSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationChoiceGroup.setOnCheckedChangeListener { _, button ->
            when (button) {
                R.id.map_rb -> {
                    SkywiseSettings.requiresLocation = true
                    MapSheet().show(this.requireActivity().supportFragmentManager, "map_sheet")
                    SkywiseSettings.setLocationType(MAP)
                }
                R.id.gps_rb -> {
                    if (LocationUtils.isLocationEnabled()) {
                        LocationUtils.getCurrentLocation()
                        SkywiseSettings.setLocationType(GPS)
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "Turn on Location",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            }
        }
    }


}