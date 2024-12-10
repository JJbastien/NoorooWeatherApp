package com.example.nooroojjbastien.weatherApiService

import com.example.nooroojjbastien.model.WeatherInformation

interface WeatherApiRepository {
    suspend fun fetchWeather(city:String): WeatherInformation
    suspend fun saveSelectedCity(city: String)
    suspend fun getSavedCity(): String?
}