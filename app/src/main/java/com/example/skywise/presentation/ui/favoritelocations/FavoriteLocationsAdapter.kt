package com.example.skywise.presentation.ui.favoritelocations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywise.R
import com.example.skywise.data.models.FavoriteLocation
import com.example.skywise.databinding.FavoriteItemLayoutBinding
import com.example.skywise.domain.utils.ConnectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteLocationsAdapter(val viewModel: FavoriteLocationsViewModel) :
    ListAdapter<FavoriteLocation, FavoriteLocationsAdapter.ViewHolder>(LocationDiffUtil()) {
    var previous = -1
    var selected = -1
    private lateinit var job: Job

    inner class ViewHolder(val binding: FavoriteItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: FavoriteItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.favorite_item_layout,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.location = current
        holder.binding.viewModel = viewModel
        holder.binding.locationCard.setOnClickListener {
            if (selected != position && ConnectionUtils.checkConnection()) {
                viewModel.getLocationData(current)
                if (::job.isInitialized)
                    job.cancel()
                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.shownLocation.collectLatest {
                        delay(150)
                        previous = selected
                        selected = position
                        notifyItemChanged(position)
                        notifyItemChanged(previous)
                    }
                }
            }
        }


        holder.binding.infoCard.visibility =
            if (selected == position) {
                View.VISIBLE
            } else {
                View.GONE
            }


        if (current.currentLocation)
            holder.binding.isCurrentTv.visibility = View.VISIBLE
        else
            holder.binding.removeButton.visibility = View.VISIBLE
    }

}


class LocationDiffUtil : DiffUtil.ItemCallback<FavoriteLocation>() {
    override fun areItemsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
        return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
    }

    override fun areContentsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
        return oldItem == newItem
    }

}

