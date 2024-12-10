package com.example.nooroojjbastien.model

data class WeatherInformation(
    val name: String,
    val temperature: Double,
    val condition: String,
    val conditionIcon: String,
    val humidity: Int,
    val uvIndex: Double,
    val feelsLike: Double
)
