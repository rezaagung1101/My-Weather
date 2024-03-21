package com.jetpack.compose.myweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetpack.compose.myweather.R
import com.jetpack.compose.myweather.adapter.CityListAdapter
import com.jetpack.compose.myweather.data.lib.City
import com.jetpack.compose.myweather.data.lib.CityDataSource
import com.jetpack.compose.myweather.databinding.ActivityListCityBinding

class ListCityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListCityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvCity.layoutManager = layoutManager

        var arrayListCity = arrayListOf<City>()
        CityDataSource.cityList.forEach{
            arrayListCity.add(it)
        }
        binding.rvCity.adapter = CityListAdapter(arrayListCity)
        binding.btnBack.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}