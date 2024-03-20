package com.jetpack.compose.myweather.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jetpack.compose.myweather.R
import com.jetpack.compose.myweather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /**
     * 1. New York 2. Singapore 3. Mumbai 4. Delhi 5. Sydney 6. Melbourne
     */
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShowList.setOnClickListener {
            startActivity(Intent(this, ListCityActivity::class.java))
        }
    }

}