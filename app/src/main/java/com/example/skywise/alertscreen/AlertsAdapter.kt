package com.example.skywise.alertscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywise.R
import com.example.skywise.databinding.AlertItemLayoutBinding

class AlertsAdapter(val viewModel: AlertsFragmentViewModel) :
    ListAdapter<WeatherAlert, AlertsAdapter.ViewHolder>(AlertsDiffUtil()) {
    lateinit var binding: AlertItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.alert_item_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        binding.weatherAlert = currentItem
        binding.removeAlertButton.setOnClickListener { viewModel.removeWeatherAlert(currentItem) }

    }

    class ViewHolder(val binding: AlertItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}

class AlertsDiffUtil : DiffUtil.ItemCallback<WeatherAlert>() {
    override fun areItemsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WeatherAlert, newItem: WeatherAlert): Boolean {
        return oldItem == newItem
    }

}