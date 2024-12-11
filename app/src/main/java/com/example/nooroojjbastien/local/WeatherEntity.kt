package com.example.nooroojjbastien.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey
    val name: String,
    val temperature: Double,
    val condition : String,
    val conditionIcon :String,
    val humidity: Int,
    val uvIndex: Double,
    val feelsLike: Double,
    val timestamp: Long = System.currentTimeMillis()
)