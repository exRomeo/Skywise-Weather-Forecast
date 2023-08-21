package com.example.skywise.presentation.ui.alertscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.databinding.FragmentAlertsBinding
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.domain.settings.SkywiseSettings.ALERT
import com.example.skywise.domain.utils.ConnectionUtils
import com.example.skywise.domain.utils.PermissionsUtil.checkOverlayPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    private val viewModel: AlertsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.adapter = AlertsAdapter(viewModel)
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.alertsList.collectLatest { binding.adapter!!.submitList(it) }
        }


        binding.setAlarm.setOnClickListener {
            if (ConnectionUtils.checkConnection())
                AlertDialog()
                    .show(requireActivity().supportFragmentManager, "alert_dialog")
            else
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.offline_message),
                    Toast.LENGTH_SHORT
                ).show()
        }
        if (SkywiseSettings.alertType == ALERT)
            checkOverlayPermission(this.requireActivity(), requireContext())
    }
}



