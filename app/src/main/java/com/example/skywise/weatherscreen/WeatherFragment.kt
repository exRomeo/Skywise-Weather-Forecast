package com.example.skywise.weatherscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentWeatherBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val repository by lazy { Repository(RetrofitClient,this.requireActivity()) }
    private val viewModel by lazy {
        ViewModelProvider(
            this.requireActivity(),
            WeatherViewModelFactory(repository)
        )[WeatherViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.lifecycleOwner = this.requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateData()
        val hourlyAdapter = HourlyAdapter()
        binding.hourlyAdapter = hourlyAdapter
        val dailyAdapter = DailyAdapter()
        binding.dailyAdapter = dailyAdapter
        lifecycleScope.launchWhenStarted {
            viewModel.weatherData.collectLatest {
                binding.weatherData = it
                hourlyAdapter.submitList(it.hourly)
                dailyAdapter.submitList(it.daily)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.snackBarText.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

}