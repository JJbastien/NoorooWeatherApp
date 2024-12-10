package com.example.nooroojjbastien.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.nooroojjbastien.model.WeatherInformation
import com.example.nooroojjbastien.utils.WeatherUiState
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.LocalTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    uiState: WeatherUiState,
    onSearch: (String) -> Unit,
    onCitySelect: (WeatherInformation) -> Unit
) {
    var selectedWeather by remember { mutableStateOf<WeatherInformation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(onSearch = onSearch)

        Spacer(modifier = Modifier.height(20.dp))

        when (uiState) {
            is WeatherUiState.NoLocation -> NoCityScreen()
            is WeatherUiState.Loading -> LoadingScreen()
            is WeatherUiState.Error -> ErrorScreen()
            is WeatherUiState.Success -> {
            selectedWeather?.let { weather ->
                WeatherDetailScreen(weather = weather)
            } ?: run {
                WeatherCard(
                    weather = uiState.weather,
                    onClick = {
                        selectedWeather = uiState.weather
                        onCitySelect(uiState.weather)
                    }
                )
            }
          }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    TextField(
        value = query,
        onValueChange = { query = it },  // Just update text, don't search
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp)),
        placeholder = {
            Text(
                text = "Search Location",
                color = Color(0xFFC4C4C4),
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        },
        singleLine = true,
        enabled = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 15.sp,
            color = Color(0xFF2C2C2C)
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF2C2C2C)
        ),
        trailingIcon = {
            IconButton(onClick = { if (query.isNotEmpty()) onSearch(query) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFC4C4C4)
                )
            }
        }
    )
}

@Composable
fun NoCityScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No city selected",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C2C2C),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun WeatherCard(
    weather: WeatherInformation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .height(117.dp)
        ) {
            Text(
                text = weather.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C2C2C)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${weather.temperature}°C",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C2C2C)
                )
                Box(
                    modifier = Modifier
                        .size(67.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF87CEEB))
                ) {
                    AsyncImage(
                        model = "https:${weather.conditionIcon}",
                        contentDescription = weather.condition,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDetailScreen(weather: WeatherInformation) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(123.dp)
                .clip(CircleShape)
                .background(Color(0xFF87CEEB))
        ) {
            AsyncImage(
                model = "https:${weather.conditionIcon}",
                contentDescription = weather.condition,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = weather.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C2C2C)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${weather.temperature}°C",
            fontSize = 70.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C2C2C)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier
                .width(274.dp)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherInfo(label = "Humidity", value = "${weather.humidity}%")
                WeatherInfo(label = "UV", value = "${weather.uvIndex}")
                WeatherInfo(label = "Feels Like", value = "${weather.feelsLike}°C")
            }
        }
    }
}

@Composable
fun WeatherInfo(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF2C2C2C).copy(alpha = 0.6f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C2C2C)
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error loading weather data",
            color = MaterialTheme.colorScheme.error
        )
    }
}