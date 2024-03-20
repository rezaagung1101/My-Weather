package com.jetpack.compose.myweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jetpack.compose.myweather.R
import com.jetpack.compose.myweather.databinding.ActivityDetailCityWeatherBinding

class DetailCityWeatherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailCityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}