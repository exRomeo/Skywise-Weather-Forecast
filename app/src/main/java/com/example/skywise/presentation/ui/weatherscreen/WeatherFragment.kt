package com.example.skywise.presentation.ui.weatherscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.databinding.FragmentWeatherBinding
import com.example.skywise.presentation.state.UIState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {


    private lateinit var binding: FragmentWeatherBinding

    private val viewModel: WeatherViewModel by viewModels()

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
        viewModel.updateData()
        lifecycleScope.launch { viewModel.weatherDTO.collectLatest { showResponse(it) } }

        lifecycleScope.launch {
            viewModel.snackBarText.collect {
                Snackbar.make(binding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showResponse(UIState: UIState) {
        when (UIState) {
            is UIState.Loading -> {
                showLoading()
            }

            is UIState.SuccessOnline -> {
                showData(UIState)
            }

            is UIState.SuccessOffline -> {
                showOfflineData(UIState)
            }

            is UIState.Failure -> {
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

    private fun showData(UIState: UIState.SuccessOnline) {
        binding.altLayout.visibility = View.GONE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
        binding.weatherData = UIState.weatherData
        binding.hourlyAdapter!!.submitList(UIState.weatherData.hourly)
        binding.dailyAdapter!!.submitList(UIState.weatherData.daily)
    }

    private fun showOfflineData(UIState: UIState.SuccessOffline) {
        binding.altLayout.visibility = View.GONE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
        binding.weatherData = UIState.weatherData
    }

    private fun showFailure() {
        binding.altLayout.visibility = View.VISIBLE
        binding.loadingAnimationb.visibility = View.GONE
        binding.errorAnimationb.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
    }

}