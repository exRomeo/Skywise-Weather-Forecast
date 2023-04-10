package com.example.skywise.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.skywise.R
import com.example.skywise.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        supportActionBar?.hide()

        binding.viewPager.adapter =
            ViewPagerAdapter(
                listOf(
                    FirstFragment(),
                    SecondFragment(),
                    ThirdFragment(),
                    ForthFragment()
                ), this.supportFragmentManager, lifecycle
            )
        binding.dotsIndicator.attachTo(binding.viewPager)

    }
}