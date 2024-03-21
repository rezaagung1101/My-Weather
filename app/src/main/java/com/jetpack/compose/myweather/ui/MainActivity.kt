package com.jetpack.compose.myweather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.jetpack.compose.myweather.BuildConfig
import com.jetpack.compose.myweather.R
import com.jetpack.compose.myweather.data.database.WeatherRecord
import com.jetpack.compose.myweather.data.remote.api.ApiConfig
import com.jetpack.compose.myweather.databinding.ActivityMainBinding
import com.jetpack.compose.myweather.utils.Constanta.PERMISSION_REQUEST_CODE
import com.jetpack.compose.myweather.utils.Helper
import com.jetpack.compose.myweather.utils.UserPreference
import com.jetpack.compose.myweather.viewModel.WeatherViewModel
import com.jetpack.compose.myweather.viewModel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val appId = BuildConfig.APP_ID
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        userPreference = UserPreference(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    weatherViewModel.apply {
                        // Pass latitude and longitude to ViewModel to fetch weather data
                        setLocation(location.latitude, location.longitude)
                        getWeatherReport(
                            latitude.value!!, longitude.value!!, appId
                        )
                    }
                    //stop location updates here if one-time location is needed
                    stopLocationUpdates()
                }
            }
        }
        getViewModel()
        // Request location updates when activity is created
        requestLocationUpdates()
        setupInformation()
    }

    private fun getViewModel(): WeatherViewModel {
        val viewModel: WeatherViewModel by viewModels {
            WeatherViewModelFactory(
                this,
                ApiConfig.getApiService(this)
            )
        }
        return viewModel
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (checkLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            requestPermissions()
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                requestLocationUpdates()
                setupInformation()
            } else {
                // Permission denied, show a message or handle accordingly
                Snackbar.make(binding.root, "Location permission denied", Snackbar.LENGTH_SHORT)
                    .show()
                binding.tvYourCityLocation.text = "Permission Denied"
            }
        }
    }

    private fun setupInformation() {
        if (checkLocationPermission()) {
            weatherViewModel.apply {
                currentCity.observe(this@MainActivity) {
                    getCityWeatherDB(it).observe(this@MainActivity) { weather ->
                        if (weather != null) {
                            binding.apply {
                                userPreference.saveCurrentCity(weather.city)
                                setupInformation(weather)
                            }
                        }
                    }
                }

                // Check if latitude and longitude have valid values
                if (latitude.value != null && longitude.value != null && latitude.value != 0.0 && longitude.value != 0.0) {
                    getWeatherReport(latitude.value!!, longitude.value!!, appId)
                } else {
                    // Request location updates if latitude or longitude is not set or is invalid
                    requestLocationUpdates()
                }
                binding.cardRefresh.setOnClickListener {
                    if (checkLocationPermission()) {
                        // Location permission is granted, so proceed with refreshing the location
                        requestLocationUpdates()
                        weatherViewModel.apply {
                            getWeatherReport(
                                latitude.value!!, longitude.value!!, appId
                            )
                        }
                    } else {
                        // Location permission is not granted, request the permission again
                        showPermissionDeniedSnackbar()
                    }
                }
                showNoInternetSnackbar.observe(this@MainActivity) { showSnackbar ->
                    if (showSnackbar) {
                        Snackbar.make(
                            binding.root,
                            "No internet connection",
                            Snackbar.LENGTH_LONG
                        ).show()
                        // Reset the value to prevent showing the Snackbar repeatedly
                        weatherViewModel.setSnackBarValue(false)
                        getCityWeatherDB(userPreference.getCurrentCity()!!).observe(this@MainActivity){
                            setupInformation(it)
                        }
                    }
                }
                isLoading.observe(this@MainActivity) {
                    showLoading(it)
                }
            }
        }
        binding.btnShowList.setOnClickListener {
            startActivity(Intent(this, ListCityActivity::class.java))
        }
    }

    private fun setupInformation(weather: WeatherRecord){
        binding.apply {
            userPreference.saveCurrentCity(weather.city)
            tvYourCityLocation.text = weather.city
            tvSkyStatus.text = weather.description
            tvMainTemperature.text =
                Helper.kelvinToCelcius(weather.temperature.toDouble())
            tvWindValue.text = weather.speed + " km/h"
            tvHumidityValue.text = weather.humidity + " hPa"
            tvPressureValue.text = weather.pressure + " %"
            tvSunriseValue.text =
                Helper.convertUnixTimeToAMPM(weather.sunrise.toInt())
            tvSunsetValue.text =
                Helper.convertUnixTimeToAMPM(weather.sunset.toInt())
            tvMinTemp.text = resources.getString(
                R.string.min_temp,
                Helper.kelvinToCelcius(weather.minTemperature.toDouble())
            )
            tvMaxTemp.text = resources.getString(
                R.string.max_temp,
                Helper.kelvinToCelcius(weather.maxTemperature.toDouble())
            )
            tvUpdatedAt.text =
                resources.getString(R.string.updated_at, weather.updatedAt)
        }
    }

    private fun showPermissionDeniedSnackbar() {
        Snackbar.make(
            binding.root,
            "Location permission denied. Please grant the permission from the app settings.",
            Snackbar.LENGTH_LONG
        )
            .setAction("Open Settings") {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .show()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}