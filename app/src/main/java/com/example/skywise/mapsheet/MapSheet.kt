package com.example.skywise.mapsheet

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.MapSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

class MapSheet : BottomSheetDialogFragment() {

    private lateinit var binding: MapSheetBinding

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
            this,
            MapSheetViewModelFactory(repository)
        )[MapSheetViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_sheet, container, false)

        (dialog as? BottomSheetDialog)?.behavior?.isDraggable = false

        Configuration.getInstance().userAgentValue = "Skywise"
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))

//        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(2.5)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        binding.addLocationButton.setOnClickListener {
            viewModel.addLocation(binding.mapView.mapCenter)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDetach()
    }

    private fun collectFlows() {
        lifecycleScope.launchWhenStarted {
            viewModel.snackBarText.collectLatest {
                Toast.makeText(
                    this@MapSheet.requireContext(),
                    getString(it), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}