package com.example.skywise.alertscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.localsource.RoomClient
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentAlertDialogBinding
import com.example.skywise.settingsscreen.SkywiseSettings
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class AlertDialog : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private var currentDay = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentHour = 0
    private var currentMinute = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var savedHour = 0
    private var savedMinute = 0

    private var isStartDate = true

    private lateinit var binding: FragmentAlertDialogBinding

    private var currentDate: Long = 0
    private var startDate: Long = 0
    private var endDate: Long = 0

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_alert_dialog, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDateFrom.setOnClickListener {
            showDatePicker()
            isStartDate = true
        }
        binding.btnDateTo.setOnClickListener {
            showDatePicker()
            isStartDate = false
        }
        binding.btnCancel.setOnClickListener { this.dismiss() }
        binding.btnSave.setOnClickListener {
            if (isValidDate()) {
                saveAlert()
                this.dismiss()
            } else
                Toast.makeText(
                    this.requireContext(),
                    getString(R.string.date_not_valid),
                    Toast.LENGTH_SHORT
                ).show()
        }
        lifecycleScope.launch {
            viewModel.alertID.collectLatest {
                makeWork(it)
            }
        }
    }

    private fun getCurrentDateTimeCalender() {
        val cal = Calendar.getInstance()
        currentDay = cal.get(Calendar.DAY_OF_MONTH)
        currentMonth = cal.get(Calendar.MONTH)
        currentYear = cal.get(Calendar.YEAR)
        currentHour = cal.get(Calendar.HOUR)
        currentMinute = cal.get(Calendar.MINUTE)
    }

    private fun showDatePicker() {
        getCurrentDateTimeCalender()
        DatePickerDialog(this.requireContext(), this, currentYear, currentMonth, currentDay).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        savedDay = day
        savedMonth = month
        savedYear = year
        getCurrentDateTimeCalender()
        TimePickerDialog(this.requireContext(), this, currentHour, currentMinute, false).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        savedHour = hour
        savedMinute = minute
        Log.i(
            "TAG",
            "onTimeSet:  $savedYear - ${savedMonth + 1} - $savedDay - $savedHour - $savedMinute"
        )


        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, savedYear)
            set(Calendar.MONTH, savedMonth)
            set(Calendar.DAY_OF_MONTH, savedDay)
            set(Calendar.HOUR_OF_DAY, savedHour)
            set(Calendar.MINUTE, savedMinute)
        }
        if (isStartDate) {
            startDate = cal.timeInMillis
            binding.btnDateFrom.text = getString(R.string.from) + getTime(startDate)
        } else {
            endDate = cal.timeInMillis

            binding.btnDateTo.text = getString(R.string.to) + getTime(endDate)

        }
    }

    private fun getTime(time: Long): String {
        val dtf =
            DateTimeFormatter.ofPattern("yyyy/MM/dd h:mm a")
                .withLocale(Locale(SkywiseSettings.lang))

        return Instant.ofEpochSecond(time / 1000).atZone(ZoneId.systemDefault())
            .format(dtf)
    }

    private fun saveAlert() {
        viewModel.addWeatherAlert(WeatherAlert(startDate = startDate, endDate = endDate))
    }


    private fun isValidDate(): Boolean {
        return startDate in (currentDate + 1) until (endDate /*- 60 * 60 * 1000*/)
    }

    private fun setPeriodicWorkManager(id: Long) {
        val data = Data.Builder().putLong("id", id).build()

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true).build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(PeriodicWorker::class.java, 24, TimeUnit.HOURS)
                .setConstraints(constraints).setInputData(data).build()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

    private fun makeWork(id: Long) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val data = Data.Builder().putString("id", id.toString()).build()
        val workRequest = OneTimeWorkRequestBuilder<NotificationAlertWorker>()
            .setConstraints(constraints)
            .setInitialDelay(
                endDate - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
            )
            .addTag(id.toString()).setInputData(data)
            .build()

        WorkManager.getInstance(this.requireContext())
            .enqueueUniqueWork(id.toString(), ExistingWorkPolicy.REPLACE, workRequest)
    }

}