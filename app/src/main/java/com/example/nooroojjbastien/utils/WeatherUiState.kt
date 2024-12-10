package com.example.nooroojjbastien.utils

import com.example.nooroojjbastien.model.WeatherInformation

sealed class WeatherUiState {
    object NoLocation : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherInformation) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
