package com.example.skywise.alertscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentAlertsBinding
import com.example.skywise.utils.ConnectionUtils
import com.example.skywise.utils.PermissionsUtil.checkOverlayPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding
    private val repository: Repository by lazy {
        Repository(RetrofitClient, RoomClient.getInstance(this.requireContext()))
    }
    private val viewModel: AlertsFragmentViewModel by lazy {
        ViewModelProvider(
            this.requireActivity(),
            AlertsFragmentViewModelFactory(repository)
        )[AlertsFragmentViewModel::class.java]
    }

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
//            makeAlert("Test")
            if (ConnectionUtils.checkConnection())
                AlertDialog().show(requireActivity().supportFragmentManager, "alert_dialog")
            else
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.offline_message),
                    Toast.LENGTH_SHORT
                ).show()
        }
        checkOverlayPermission(this.requireActivity(), requireContext())
    }


/*    fun checkOverlayPermission(activity: Activity, context: Context) {
        PermissionsUtil.checkOverlayPermission(activity, context)
    }*/

    private fun makeAlert(description: String) {
        val intent = Intent(requireContext(), AlertService::class.java)
        intent.putExtra("Stringayte", description)
        ContextCompat.startForegroundService(requireContext(), intent)
    }

}



