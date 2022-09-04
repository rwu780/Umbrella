package com.example.umbrella.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umbrella.network.WeatherApi
import com.example.umbrella.network.WeatherDaily
import com.example.umbrella.network.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val ZIP_CODE = "ZIP_CODE"
const val TEMPERATURE = "TEMPERATURE_MODE"

enum class Temperature_Unit(val unit: String) {
    Fahrenheit("imperial"),
    Celeuis("metric"),
    Kelvin("Standard")

}

private const val TAG = "WeatherViewModel"
class WeatherViewModel: ViewModel() {

    private val _zip = MutableLiveData<String>()
    val zip : LiveData<String> get() = _zip

    private val _unit = MutableLiveData<Temperature_Unit>()
    val unit : LiveData<Temperature_Unit> get() {
        if (_unit.value == null)
            _unit.value = Temperature_Unit.Fahrenheit
        return _unit
    }

    private val _temperature = MutableLiveData<Float>()
    val temperature : LiveData<Float> get() = _temperature

    private val _description = MutableLiveData<String>()
    val description : LiveData<String> get() = _description

    private val _weatherResponse = MutableLiveData<List<WeatherDaily>>()
    val weatherResponse: LiveData<List<WeatherDaily>> get() = _weatherResponse

    private val _city = MutableLiveData<String>()
    val city : LiveData<String> get() = _city

    private var _response : WeatherResponse? = null


    fun setZip(zipCode: String){
        _zip.value = zipCode
        getWeather()
    }

    fun setCity(cityName: String){
        _city.value = cityName
    }

    fun setUnit(string: String){
        when(string){
            Temperature_Unit.Fahrenheit.toString() -> _unit.value = Temperature_Unit.Fahrenheit
            Temperature_Unit.Celeuis.toString() -> _unit.value = Temperature_Unit.Celeuis
            Temperature_Unit.Kelvin.toString() -> _unit.value = Temperature_Unit.Kelvin
            else -> _unit.value = Temperature_Unit.Fahrenheit
        }
        getWeather()
    }

    private fun formatZip() = _zip.value + ",us"

    fun getWeather() {
        Log.d(TAG, "getWeather: ")
        viewModelScope.launch {

            try {
                _response = WeatherApi.retrofitService.getWeathers(
                    formatZip(),
                    units = unit.value?.unit.toString()
                )
                _city.value = _response!!.city.name
                _temperature.value = _response!!.list[0].main.temp
                _description.value = _response!!.list[0].weather[0].main

                withContext(Dispatchers.Default){
                    _weatherResponse.postValue(filterOutput())
                }


            } catch (exception: Exception) {
                _description.value = "Error, please check your settings"
                _city.value = ""
                _temperature.value = 0.00f
                _weatherResponse.value = emptyList()
                _response = null
                exception.printStackTrace()
            }
        }
    }

    @SuppressLint("NewApi")
    fun filterOutput() : List<WeatherDaily> {
        val mList = mutableListOf<WeatherDaily>()

        val input_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val output_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")

        _response!!.list.forEach { weatherItem ->

            val dateTime = LocalDateTime.parse(weatherItem.dt_txt, input_formatter)
            val output_date = dateTime.format(output_formatter).toString()

            val (date_s, time_s) = output_date.split("\\s+".toRegex(), 2)
            val imageUrl =
                "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}@2x.png"
            val newItem = WeatherDaily(date_s, time_s, weatherItem.main.temp, imageUrl, )

            mList.add(newItem)

        }

        _response = null

        findHottestAndColdest(mList)

        return mList
    }

    private fun findHottestAndColdest(mList: List<WeatherDaily>) {
        val maps = mList.groupBy { it.date }.filterValues {it.isNotEmpty()}

        for (i in maps.keys){
            val coldest = maps[i]?.map { it.temperature }?.minOrNull()
            val hottest = maps[i]?.map { it.temperature }?.maxOrNull()

            for (j in maps[i]!!){
                if (j.temperature == coldest){
                    j.isColdest = true
                }
                if (j.temperature == hottest){
                    j.isWarmest = true
                }
            }
        }
    }
}