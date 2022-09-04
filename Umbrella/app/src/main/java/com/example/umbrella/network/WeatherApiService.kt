package com.example.umbrella.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.openweathermap.org/"
private const val END_POINT = "data/2.5/forecast"
private const val API_KEY = "bb8d40aca87e5095d58f2729a17f72b8"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface WeatherApiService {

    @GET(END_POINT)
    suspend fun getWeathers(
        @Query("zip") zip_code: String,
        @Query("units") units: String,
        @Query("appid") api_key: String = API_KEY,
        ) : WeatherResponse
}

object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}