package com.example.skywise.favoritelocations

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
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentFavoriteLocationsBinding
import com.example.skywise.mapsheet.MapSheet
import com.example.skywise.utils.ConnectionUtils
import com.google.android.material.snackbar.Snackbar


class FavoriteLocationsFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteLocationsBinding
    private val repository by lazy {
        Repository(
            RetrofitClient,
            RoomClient.getInstance(this.requireActivity().applicationContext),
            this.requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
        )
    }
    private val viewModel: FavoriteLocationsViewModel by lazy {
        ViewModelProvider(
            this.requireActivity(), FavoriteLocationViewModelFactory(repository)
        )[FavoriteLocationsViewModel::class.java]
    }

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
        lifecycleScope.launchWhenStarted {
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
                Snackbar.make(binding.root, "you're currently offline!", Snackbar.LENGTH_SHORT)
                    .show()
        }
    }


}