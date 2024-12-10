package com.example.nooroojjbastien.model


import com.example.nooroojjbastien.model.Current
import com.example.nooroojjbastien.model.Location
import com.google.gson.annotations.SerializedName

data class WeatherApiResponse(
    @SerializedName("current")
    val current: Current?,
    @SerializedName("location")
    val location: Location?
)