package com.example.skywise.favoritelocations

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skywise.R
import com.example.skywise.data.FavoriteLocation
import com.example.skywise.databinding.FavoriteItemLayoutBinding
import com.example.skywise.utils.ConnectionUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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
                Log.i("TAG", "onBindViewHolder: Clicked")
                viewModel.getLocationData(current)
                if (::job.isInitialized)
                    job.cancel()
                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.shownLocation.debounce(150).collectLatest() {
                        Log.i("TAG", "onBindViewHolder: Listening from $position - ${current.area}")
                        previous = selected
                        selected = position
                        notifyItemChanged(position)
                        notifyItemChanged(previous)
                    }
                }
            }
        }
        Log.i("TAG", "onBindViewHolder: $position")


        holder.binding.infoCard.visibility =
            if (selected == position) {
                Log.i("TAG", ":  ")
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
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
        return oldItem == newItem
    }

}

