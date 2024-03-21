package com.jetpack.compose.myweather.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jetpack.compose.myweather.data.database.WeatherDatabase
import com.jetpack.compose.myweather.data.remote.api.ApiService
import com.jetpack.compose.myweather.data.remote.repository.WeatherRepository

//class WeatherViewModelFactory {
//}
class WeatherViewModelFactory(val context: Context, private val apiService: ApiService) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val database = WeatherDatabase.getDatabase(context)
            return WeatherViewModel(
                WeatherRepository(
                database,
                apiService
            )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}