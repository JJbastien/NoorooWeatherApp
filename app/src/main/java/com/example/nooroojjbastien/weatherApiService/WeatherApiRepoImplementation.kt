package com.example.nooroojjbastien.weatherApiService

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.noorooweatherapp.BuildConfig
import com.example.nooroojjbastien.model.WeatherInformation
import com.example.nooroojjbastien.utils.toWeatherInformation
import com.example.nooroojjbastien.local.WeatherDao
import com.example.nooroojjbastien.utils.toWeatherEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WeatherApiRepoImplementation @Inject constructor(
    private val weatherApiService: WeatherApiNetwork,
    private val weatherDao: WeatherDao,
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
    ):
    WeatherApiRepository {

    companion object {
        private val SELECTED_CITY = stringPreferencesKey("selected")
        private const val CACHE_DURATION = 30 * 60 * 1000
    }

    override suspend fun fetchWeather(city:String): WeatherInformation {
        try {
            val cachedWeather = weatherDao.getWeatherForCity(city)
            if (cachedWeather != null && isCacheValid(cachedWeather.timestamp)) {
                return cachedWeather.toWeatherInformation()
            }

            val response = weatherApiService.callWeatherApi(BuildConfig.API_KEY, city)
            val weatherInfo = response.toWeatherInformation()

            weatherDao.insertWeather(weatherInfo.toWeatherEntity())

            return weatherInfo
        } catch (e: Exception) {
            // If API call fails and we have cached data, return it regardless of age
            val cachedWeather = weatherDao.getWeatherForCity(city)
            if (cachedWeather != null) {
                return cachedWeather.toWeatherInformation()
            }
            throw Exception("Failed to fetch weather data", e)
        }
    }

    override suspend fun saveSelectedCity(city: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_CITY] = city
        }
    }

    override suspend fun getSavedCity(): String? {
        return try {
            dataStore.data.first()[SELECTED_CITY]
        } catch (e: Exception) {
            null
        }
    }

    private fun isCacheValid(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp < CACHE_DURATION
    }
}