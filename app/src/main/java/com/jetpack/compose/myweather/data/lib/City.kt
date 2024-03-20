package com.jetpack.compose.myweather.data.lib

import android.os.Parcelable
import com.jetpack.compose.myweather.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val name: String,
    val image: Int
): Parcelable

object CityDataSource{
    val cityList = listOf(
        City("New York", R.drawable.new_york_city),
        City("Singapore", R.drawable.singapore_city),
        City("Mumbai", R.drawable.mumbai_city),
        City("Delhi", R.drawable.delhi_city),
        City("Sydney", R.drawable.sydney_city),
        City("Melbourne", R.drawable.melbourne_city),
    )
}