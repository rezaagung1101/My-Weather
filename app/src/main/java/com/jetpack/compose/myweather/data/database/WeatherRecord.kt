package com.jetpack.compose.myweather.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "weather")
//data class WeatherRecord(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long,
//    @ColumnInfo(name = "city")
//    var city: String,
//    @ColumnInfo(name = "description")
//    var description: String,
//    @ColumnInfo(name = "temperature")
//    var temperature: String,
//    @ColumnInfo(name = "speed")
//    var speed: String,
//    @ColumnInfo(name = "country")
//    var country: String
//)

@Entity(tableName = "weather")
data class WeatherRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "city")
    var city: String,
    @ColumnInfo(name = "updatedAt")
    var updatedAt: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "temperature")
    var temperature: String,
    @ColumnInfo(name = "min_temperature")
    var minTemperature: String,
    @ColumnInfo(name = "max_temperature")
    var maxTemperature: String,
    @ColumnInfo(name = "sunrise")
    var sunrise: String,
    @ColumnInfo(name = "sunset")
    var sunset: String,
    @ColumnInfo(name = "speed")
    var speed: String,
    @ColumnInfo(name = "pressure")
    var pressure: String,
    @ColumnInfo(name = "humidity")
    var humidity: String,
    @ColumnInfo(name = "country")
    var country: String
)