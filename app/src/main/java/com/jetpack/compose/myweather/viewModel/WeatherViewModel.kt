package com.jetpack.compose.myweather.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpack.compose.myweather.data.database.WeatherRecord
import com.jetpack.compose.myweather.data.remote.repository.WeatherRepository
import com.jetpack.compose.myweather.utils.Helper
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var _latitude = MutableLiveData<Double>(0.0)
    val latitude: LiveData<Double> = _latitude
    private var _longitude = MutableLiveData<Double>(0.0)
    val longitude: LiveData<Double> = _longitude
    private val _showNoInternetSnackbar = MutableLiveData<Boolean>()
    private var _currentCity = MutableLiveData<String>()
    val currentCity: LiveData<String> = _currentCity
    val showNoInternetSnackbar: LiveData<Boolean>
        get() = _showNoInternetSnackbar

    fun setLocation(latitude: Double, longitude: Double) {
        _latitude.value = latitude
        _longitude.value = longitude
    }

    fun getCityWeatherDB(city: String): LiveData<WeatherRecord> {
        return repository.getCityWeather(city)
    }

    fun getWeatherReport(latitude: Double, longitude: Double, appId: String) =
        viewModelScope.launch {
            try {
                repository.getCurrentWeather(latitude.toString(), longitude.toString(), appId)
                    .let { response ->
                        if (response.isSuccessful) {
                            _currentCity.value = response.body()?.name.toString()
                                repository.insertWeather(
                                    WeatherRecord(
                                        city = response.body()?.name.toString(),
                                        updatedAt = Helper.convertTimezoneToString(response.body()?.timezone!!),
                                        description = response.body()?.weather!![0].description,
                                        temperature = response.body()?.main!!.temp.toString(),
                                        minTemperature = response.body()?.main!!.temp_min.toString(),
                                        maxTemperature = response.body()?.main!!.temp_max.toString(),
                                        country = response.body()?.sys!!.country,
                                        humidity = response.body()?.main!!.humidity.toString(),
                                        pressure = response.body()?.main!!.pressure.toString(),
                                        speed = response.body()?.wind!!.speed.toString(),
                                        sunrise = response.body()?.sys!!.sunrise.toString(),
                                        sunset = response.body()?.sys!!.sunset.toString()
                                    )
                                )
//                            }
                        } else {
                            Log.d("TAG", "GET Weather Error Code: ${response.code()}")
                        }
                    }
            } catch (e: UnknownHostException) {
                // Handle the exception, for example, show a message to the user indicating no internet connection
                Log.e("TAG", "Network error: ${e.message}")
                setSnackBarValue(true)
            }
        }

    fun getCityWeather(city: String, appId: String) =
        viewModelScope.launch {
            try {
                repository.getCityCurrentWeather(city, appId)
                    .let { response ->
                        if (response.isSuccessful) {
                            repository.insertWeather(
                                WeatherRecord(
//                                    id = 0,
                                    city = response.body()?.name.toString(),
                                    updatedAt = Helper.convertTimezoneToString(response.body()?.timezone!!),
                                    description = response.body()?.weather!![0].description,
                                    temperature = response.body()?.main!!.temp.toString(),
                                    minTemperature = response.body()?.main!!.temp_min.toString(),
                                    maxTemperature = response.body()?.main!!.temp_max.toString(),
                                    country = response.body()?.sys!!.country,
                                    humidity = response.body()?.main!!.humidity.toString(),
                                    pressure = response.body()?.main!!.pressure.toString(),
                                    speed = response.body()?.wind!!.speed.toString(),
                                    sunrise = response.body()?.sys!!.sunrise.toString(),
                                    sunset = response.body()?.sys!!.sunset.toString()
                                )
                            )
//                            }
                        } else {
                            Log.d("TAG", "GET Weather Error Code: ${response.code()}")
                        }
                    }
            } catch (e: UnknownHostException) {
                // Handle the exception, for example, show a message to the user indicating no internet connection
                Log.e("TAG", "Network error: ${e.message}")
                setSnackBarValue(true)
            }
        }

    fun setSnackBarValue(status: Boolean) {
        _showNoInternetSnackbar.value = status
    }


}