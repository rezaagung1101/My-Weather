package com.dicoding.storyapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jetpack.compose.myweather.data.database.WeatherRecord

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(weatherRecord: WeatherRecord)

    @Query("SELECT * FROM weather ORDER BY id DESC")
    fun getWeatherRecord(): LiveData<WeatherRecord>

}