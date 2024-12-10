package com.example.nooroojjbastien.weatherApiService

import com.example.nooroojjbastien.model.WeatherApiResponse
import com.example.nooroojjbastien.utils.WeatherApiStringDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiNetwork {

    @GET(WeatherApiStringDetails.WEATHER)
    suspend fun callWeatherApi(
        @Query("key") apiKey : String,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "no"
    ): WeatherApiResponse
}

