package com.example.skywise.presentation.ui.favoritelocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.databinding.FragmentFavoriteLocationsBinding
import com.example.skywise.domain.utils.ConnectionUtils
import com.example.skywise.presentation.ui.mapsheet.MapSheet
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class FavoriteLocationsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteLocationsBinding

    private val viewModel: FavoriteLocationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorite_locations, container, false
        )
        binding.lifecycleOwner = this.requireActivity()
        binding.viewModel = viewModel
        binding.adapter = FavoriteLocationsAdapter(viewModel)
        viewModel.getFavoriteLocations()
        lifecycleScope.launch{
            viewModel.favoriteLocation.collect { binding.adapter!!.submitList(it) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.floatingButton.setOnClickListener {
            if (ConnectionUtils.checkConnection())
                MapSheet()
                    .show(
                        this.requireActivity()
                            .supportFragmentManager,
                        "mapFragment"
                    )
            else
                Snackbar.make(binding.root, getString(R.string.offline_message), Snackbar.LENGTH_SHORT)
                    .show()
        }
    }
}