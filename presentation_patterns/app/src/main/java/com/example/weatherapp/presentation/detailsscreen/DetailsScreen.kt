package com.example.weatherapp.presentation.detailsscreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.domain.ApiResult
import com.example.weatherapp.presentation.compose.ErrorItem
import com.example.weatherapp.presentation.compose.LoadingView

@Composable
fun DetailsScreen(
    cityName: String, viewModel: DetailsViewModel = hiltViewModel()
) {
    viewModel.getWeather(cityName)
    val weather = viewModel.detailsFlow.collectAsState().value
    
    Log.d("ZZZ", "Details - ${viewModel.hashCode()}")
    
    when (weather) {
        is ApiResult.Error -> {
            ErrorItem(message = weather.exception,
                modifier = Modifier.fillMaxSize(),
                onClickRetry = { viewModel.getWeather(cityName) })
        }
        is ApiResult.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        else -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                weather.data?.let {
                    
                    Text(text = it.cityName, style = MaterialTheme.typography.titleLarge)
                    Text(text = it.countryName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = it.temp.toString() + " ºC",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(text = it.condition, style = MaterialTheme.typography.titleLarge)
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind),
                            contentDescription = "wind",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "  wind " + it.windKmh,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}