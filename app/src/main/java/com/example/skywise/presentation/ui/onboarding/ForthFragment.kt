package com.example.skywise.presentation.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.skywise.R
import com.example.skywise.databinding.FragmentForthBinding
import com.example.skywise.domain.settings.SkywiseSettings
import com.example.skywise.presentation.ui.main.MainActivity


class ForthFragment : Fragment() {
    lateinit var binding: FragmentForthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forth, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getStartedBtn.setOnClickListener {
            SkywiseSettings.doneFirstLaunch()
            startActivity(Intent(this.requireContext(), MainActivity::class.java))

        }
    }
}