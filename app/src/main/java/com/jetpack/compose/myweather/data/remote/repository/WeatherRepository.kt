package com.jetpack.compose.myweather.data.remote.repository

import androidx.lifecycle.LiveData
import com.jetpack.compose.myweather.data.database.WeatherDatabase
import com.jetpack.compose.myweather.data.database.WeatherRecord
import com.jetpack.compose.myweather.data.remote.api.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WeatherRepository(private val database: WeatherDatabase, private val apiService: ApiService) {
    suspend fun getCurrentWeather(lat: String, long: String, app_id: String) =
        apiService.getCurrentWeather(lat, long, app_id)

    suspend fun getCityCurrentWeather(city: String,app_id: String) =
        apiService.getCityCurrentWeather(city, app_id)

    fun insertWeather(weather: WeatherRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            database.weatherDao().insertWeather(weather)
        }
    }

    fun getCityWeather(city: String): LiveData<WeatherRecord> {
        return database.weatherDao().getCityWeather(city)
    }
}