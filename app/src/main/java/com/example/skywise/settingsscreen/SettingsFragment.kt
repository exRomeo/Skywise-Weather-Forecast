package com.example.skywise.settingsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skywise.IMPERIAL
import com.example.skywise.METRIC
import com.example.skywise.R
import com.example.skywise.STANDARD
import com.example.skywise.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel by lazy { ViewModelProvider(this.requireActivity())[SettingsViewModel::class.java] }
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_settings,
                container,
                false
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
                R.id.metric_radioButton -> viewModel.setMetric()
                R.id.standard_radioButton -> viewModel.setStandard()
                R.id.imperial_radioButton -> viewModel.setImperial()
            }
        }
    }


}