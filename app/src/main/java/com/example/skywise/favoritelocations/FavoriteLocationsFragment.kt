package com.example.skywise.favoritelocations

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skywise.R
import com.example.skywise.data.Repository
import com.example.skywise.data.remotesource.RetrofitClient
import com.example.skywise.databinding.FragmentFavoriteLocationsBinding
import java.util.zip.Inflater

class FavoriteLocationsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteLocationsBinding
    private val repository by lazy {
        Repository(
            RetrofitClient, this.requireActivity().getPreferences(
                Context.MODE_PRIVATE
            )
        )
    }
    private val viewModel: FavoriteLocationsViewModel by lazy {
        ViewModelProvider(
            this.requireActivity(),
            FavoriteLocationViewModelFactory(repository)
        )[FavoriteLocationsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite_locations,container,false)
        binding.lifecycleOwner = this.requireActivity()
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW,)
//            Uri.parse("geo:0,0?q=")
//            startActivityForResult(intent,1)
//            requestLocation.launch(intent)
        }
    }

/*    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("TAG", "onActivityResult: ?!!!!")
        if (requestCode ==1 && resultCode == Activity.RESULT_OK) {
            val location = data?.data.toString()
            Log.i("TAG", "onActivityResult: GOT A LOCATION ->>>>>>>>> $location")
        }
    }*/

/*
    private val requestLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("TAG", "onActivityResult: ?!!!!")
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val location = data?.data.toString()
            Log.i("TAG", "onActivityResult: GOT A LOCATION ->>>>>>>>> $location")
            // do something with the selected location
        }
    }
*/

//    private val requestLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val data: Intent? = result.data
//            val locationUri = data?.data
//            if (locationUri != null) {
//                val locationData = getLocationDataFromUri(locationUri)
//                if (locationData != null) {
//                    // do something with the location data
//                } else {
//                    Log.e("TAG", "Invalid location data")
//                }
//            } else {
//                Log.e("TAG", "Location data is null")
//            }
//        } else {
//            Log.e("TAG", "Location selection cancelled")
//        }
//    }
//    private fun getLocationDataFromUri(uri: Uri): LocationData? {
//        val latitude = uri.getQueryParameter("lat")?.toDoubleOrNull()
//        val longitude = uri.getQueryParameter("lon")?.toDoubleOrNull()
//        if (latitude != null && longitude != null) {
//            return LocationData(latitude, longitude)
//        }
//        return null
//    }
//    data class LocationData(val latitude: Double, val longitude: Double)

}