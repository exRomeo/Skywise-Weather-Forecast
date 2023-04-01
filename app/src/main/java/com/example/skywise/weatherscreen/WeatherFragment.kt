package com.example.skywise.weatherscreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.WeatherDTO
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentWeatherBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class WeatherFragment : Fragment() {


    private lateinit var binding: FragmentWeatherBinding
    private val repository by lazy {
        Repository(
            RetrofitClient,
            RoomClient.getInstance(this.requireActivity().applicationContext),
            this.requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this.requireActivity(), WeatherViewModelFactory(repository)
        )[WeatherViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.requireActivity()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hourlyAdapter = HourlyAdapter()
        binding.dailyAdapter = DailyAdapter()

        lifecycleScope.launchWhenStarted { viewModel.weatherDTO.collectLatest { showResponse(it) } }

        lifecycleScope.launchWhenStarted {
            viewModel.snackBarText.collect {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateData()
    }
    private fun showResponse(weatherDTO: WeatherDTO) {
        when (weatherDTO) {
            is WeatherDTO.Loading -> {
                showLoading()
            }
            is WeatherDTO.SuccessOnline -> {
                showData(weatherDTO)
            }
            is WeatherDTO.SuccessOffline -> {
                showOfflineData(weatherDTO)
            }
            is WeatherDTO.Failure -> {
                showFailure()
            }
        }
    }

    private fun showLoading() {
        binding.altLayout.visibility = View.VISIBLE
        binding.loadingAnimationb.visibility = View.VISIBLE
        binding.errorAnimationb.visibility = View.GONE
        binding.mainLayout.visibility = View.GONE
    }

    private fun showData(weatherDTO: WeatherDTO.SuccessOnline) {
        binding.altLayout.visibility = View.GONE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
        binding.weatherData = weatherDTO.weatherData
        binding.hourlyAdapter!!.submitList(weatherDTO.weatherData.hourly)
        binding.dailyAdapter!!.submitList(weatherDTO.weatherData.daily)
    }

    private fun showOfflineData(weatherDTO: WeatherDTO.SuccessOffline) {
        binding.altLayout.visibility = View.GONE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
        binding.weatherData = weatherDTO.weatherData
    }

    private fun showFailure() {
        binding.altLayout.visibility = View.VISIBLE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
    }

}