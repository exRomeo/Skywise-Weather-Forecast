package com.example.skywise.weatherscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywise.R
import com.example.skywise.data.Hourly
import com.example.skywise.databinding.HourlyItemLayoutBinding

class HourlyAdapter : ListAdapter<Hourly, HourlyAdapter.ViewHolder>(HourlyDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: HourlyItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.hourly_item_layout, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.hourly = current
    }

    inner class ViewHolder(var binding: HourlyItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class HourlyDiffUtil : DiffUtil.ItemCallback<Hourly>() {

    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean =
        oldItem.dt == newItem.dt

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean =
        oldItem == newItem
}