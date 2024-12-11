package com.example.nooroojjbastien.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table WHERE name = :city")
    suspend fun getWeatherForCity(city: String): WeatherEntity?

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Query("DELETE FROM weather_table WHERE name = :city")
    suspend fun deleteWeatherForCity(city: String)
}