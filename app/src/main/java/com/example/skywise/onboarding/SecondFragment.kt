package com.example.skywise.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.skywise.R
import com.example.skywise.databinding.FragmentSecondBinding
import com.example.skywise.mapsheet.MapSheet
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.settingsscreen.SkywiseSettings.GPS
import com.example.skywise.settingsscreen.SkywiseSettings.MAP
import com.example.skywise.utils.LocationUtils


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
                    LocationUtils.requestLocationPermissions(this.requireActivity())
                    LocationUtils.getCurrentLocation()
                    SkywiseSettings.setLocationType(GPS)
                }
            }
        }
    }

}