package com.example.nooroojjbastien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.nooroojjbastien.theme.NoorooWeatherAppTheme
import com.example.nooroojjbastien.utils.WeatherUiState
import com.example.nooroojjbastien.view.WeatherApp
import com.example.nooroojjbastien.viewModel.WeatherApiViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherApiViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val weatherState by viewModel.weather.collectAsState()

            NoorooWeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp(
                        uiState = when (weatherState) {
                            is WeatherUiState.NoLocation -> WeatherUiState.NoLocation
                            is WeatherUiState.Loading -> WeatherUiState.Loading
                            is WeatherUiState.Error -> WeatherUiState.Error((weatherState as WeatherUiState.Error).message)
                            is WeatherUiState.Success -> WeatherUiState.Success((weatherState as WeatherUiState.Success).weather)

                        },
                        onSearch = { query ->
                            viewModel.onSearchQueryChange(query)
                            viewModel.searchCity(query)
                        },
                        onCitySelect = { weatherResponse ->
                            viewModel.searchCity(weatherResponse.name)
                        }
                    )
                }
            }
        }
    }
}
