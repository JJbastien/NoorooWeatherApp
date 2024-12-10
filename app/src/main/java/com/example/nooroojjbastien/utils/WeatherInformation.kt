package com.example.nooroojjbastien.utils

import com.example.nooroojjbastien.model.WeatherApiResponse
import com.example.nooroojjbastien.model.WeatherInformation

fun WeatherApiResponse.toWeatherInformation(): WeatherInformation {
    return WeatherInformation(
        name = location?.name ?:" ",
        temperature = current?.tempC ?: -999.0,
        condition = current?.condition?.text ?:" ",
        conditionIcon = current?.condition?.icon ?:" ",
        humidity = current?.humidity?: -1,
        uvIndex = current?.uv ?: -999.0,
        feelsLike = current?.feelslikeC ?: -999.0
    )
}