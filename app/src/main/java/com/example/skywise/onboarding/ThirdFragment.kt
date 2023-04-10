package com.example.skywise.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.skywise.R
import com.example.skywise.databinding.FragmentThirdBinding
import com.example.skywise.settingsscreen.SkywiseSettings
import com.example.skywise.settingsscreen.SkywiseSettings.ALERT
import com.example.skywise.settingsscreen.SkywiseSettings.NOTIFICATION
import com.example.skywise.utils.NotificationUtils
import com.example.skywise.utils.PermissionsUtil


class ThirdFragment : Fragment() {
    lateinit var binding: FragmentThirdBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_third,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertsChoiceGroup.setOnCheckedChangeListener { _, button ->
            when (button) {
                R.id.notification_rb -> {
                    NotificationUtils.requestNotificationsPermission(this.requireActivity())
                    SkywiseSettings.setAlertType(NOTIFICATION)
                }
                R.id.pop_up_rb -> {
                    PermissionsUtil.checkOverlayPermission(
                        this.requireActivity(),
                        this.requireContext()
                    )
                    SkywiseSettings.setAlertType(ALERT)
                }

            }
        }
    }
}