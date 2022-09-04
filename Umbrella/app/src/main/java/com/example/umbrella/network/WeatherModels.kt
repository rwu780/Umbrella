package com.example.umbrella.network

import android.graphics.Color
import java.math.BigInteger
import java.util.*

data class WeatherResponse(
    val list: List<WeatherItem>,
    val city: City
)

data class City (
    val name: String,
    val country: String
)

data class WeatherItem(
    val dt_txt: String,
    val main: WeatherMain,
    val weather: List<WeatherDescription>
)

class WeatherDescription(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

class WeatherMain(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float
)

data class WeatherDaily(
    val date: String,
    val time: String,
    val temperature: Float,
    val iconUrl: String,
    var isColdest: Boolean = false,
    var isWarmest: Boolean = false,
    var colorCode: Int? = null
) {
    fun temperatureString() : String = temperature.toString()

}


