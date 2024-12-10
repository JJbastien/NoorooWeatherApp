package com.example.nooroojjbastien.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nooroojjbastien.utils.WeatherUiState
import com.example.nooroojjbastien.weatherApiService.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherApiViewModel @Inject constructor(
    private val weatherRepository: WeatherApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherUiState>(WeatherUiState.NoLocation)
    val weather: StateFlow<WeatherUiState> = _weather.asStateFlow()

    fun onSearchQueryChange(query: String) {
        if (query.isBlank()) {
            _weather.update { WeatherUiState.NoLocation }
        }
    }

    fun searchCity(city: String) {
        if (city.isBlank()) {
            _weather.update { WeatherUiState.NoLocation }
            return
        }

        viewModelScope.launch(dispatcher) {
            _weather.update { WeatherUiState.Loading }
            try {
                val weather = weatherRepository.fetchWeather(city)
                _weather.update { WeatherUiState.Success(weather) }
                weatherRepository.saveSelectedCity(city)
            } catch (e: Exception) {
                _weather.update { WeatherUiState.Error(e.message ?: "Failed to fetch weather data") }
            }
        }
    }

    init {
        viewModelScope.launch(dispatcher) {
            try {
                val savedCity = weatherRepository.getSavedCity()
                if (!savedCity.isNullOrBlank()) {
                    searchCity(savedCity)
                }
            } catch (e: Exception) {
                _weather.update { WeatherUiState.Error("Failed to load saved city") }
            }
        }
    }
}
