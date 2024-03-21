package com.jetpack.compose.myweather.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jetpack.compose.myweather.BuildConfig
import com.jetpack.compose.myweather.R
import com.jetpack.compose.myweather.data.lib.City
import com.jetpack.compose.myweather.data.remote.api.ApiConfig
import com.jetpack.compose.myweather.databinding.ActivityDetailCityWeatherBinding
import com.jetpack.compose.myweather.utils.Constanta
import com.jetpack.compose.myweather.utils.Helper
import com.jetpack.compose.myweather.viewModel.WeatherViewModel
import com.jetpack.compose.myweather.viewModel.WeatherViewModelFactory

class DetailCityWeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCityWeatherBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val appId = BuildConfig.APP_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val city = intent.getParcelableExtra<City>(Constanta.city) as City
        getViewModel()
        setupInformation(city)
        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
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

    fun setupInformation(city: City?) {
        if (city != null) {
            binding.tvYourCityLocation.text = city.name
            weatherViewModel.getCityWeather(city.name, appId)
            weatherViewModel.apply {
                getCityWeatherDB(city.name).observe(this@DetailCityWeatherActivity) { weather ->
                    if (weather != null) {
                        binding.apply {
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
                }
                isLoading.observe(this@DetailCityWeatherActivity){
                    showLoading(it)
                }
            }
        }
        binding.cardRefresh.setOnClickListener {
            setupInformation(city)
        }
    }
    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}