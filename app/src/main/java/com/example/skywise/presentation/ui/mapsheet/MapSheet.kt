package com.example.skywise.presentation.ui.mapsheet

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.databinding.MapSheetBinding
import com.example.skywise.domain.settings.SkywiseSettings
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

@AndroidEntryPoint

class MapSheet : BottomSheetDialogFragment() {

    private lateinit var binding: MapSheetBinding

    private val viewModel: MapSheetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_sheet, container, false)

        (dialog as? BottomSheetDialog)?.behavior?.isDraggable = false

        Configuration.getInstance().userAgentValue = "Skywise"
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))

        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(2.5)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        binding.addLocationButton.setOnClickListener {
            if (SkywiseSettings.requiresLocation) {
                viewModel.updateLocation(binding.mapView.mapCenter)
            } else {
                viewModel.addLocation(binding.mapView.mapCenter)
            }
            this.dismiss()
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