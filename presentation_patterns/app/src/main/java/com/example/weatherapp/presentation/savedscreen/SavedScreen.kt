package com.example.weatherapp.presentation.savedscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.data.locale.db.WeatherItem

@Composable
fun SavedScreen(
    startTransition: (String) -> Unit,
    viewModel: SavedViewModel = hiltViewModel()
) {
    
    Log.d("ZZZ", "Saved - ${viewModel.hashCode()}")
    
    val lazyWeatherItems = viewModel.whetherItemsFlow.collectAsState(initial = emptyList())
    
    LazyColumn {
        items(lazyWeatherItems.value) { weatherItem ->
            WeatherLazyItem(
                weatherItem = weatherItem,
                onDeleteClick = { viewModel.deleteWeatherItem(weatherItem) },
                onItemClick = { startTransition(weatherItem.cityName) }
            )
        }
    }
}

@Composable
fun WeatherLazyItem(
    weatherItem: WeatherItem,
    onItemClick: () -> Unit = {},
    onDeleteClick: (WeatherItem) -> Unit = {}
) {
    
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick() })
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = weatherItem.cityName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1
        )
        Text(
            text = weatherItem.lastUpdated,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1
        )
        IconButton(
            onClick = { onDeleteClick(weatherItem) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete request"
            )
        }
    }
}