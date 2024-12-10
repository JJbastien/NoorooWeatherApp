package com.example.nooroojjbastien.weatherApiService

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.noorooweatherapp.BuildConfig
import com.example.nooroojjbastien.model.WeatherInformation
import com.example.nooroojjbastien.utils.toWeatherInformation
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WeatherApiRepoImplementation @Inject constructor(
    private val weatherApiService: WeatherApiNetwork,
    private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
    ):
    WeatherApiRepository {

    companion object {
        private val SELECTED_CITY = stringPreferencesKey("selected")
    }

    override suspend fun fetchWeather(city:String): WeatherInformation {
        return try {
            val response = weatherApiService.callWeatherApi(BuildConfig.API_KEY, city)
            response.toWeatherInformation()
        } catch (e: Exception) {
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
}