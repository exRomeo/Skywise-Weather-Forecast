package com.example.skywise.weatherscreen

import android.content.Context
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
import kotlinx.coroutines.flow.collectLatest

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val repository by lazy {
        Repository(
            RetrofitClient, this.requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
        )
    }
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateData()
        binding.hourlyAdapter = HourlyAdapter()
        binding.dailyAdapter = DailyAdapter()
        lifecycleScope.launchWhenStarted {
            viewModel.weatherData.collectLatest {
                binding.hourlyAdapter!!.submitList(it.hourly)
                binding.dailyAdapter!!.submitList(it.daily)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.snackBarText.collectLatest {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        }

    }

}