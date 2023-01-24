package com.example.weatherapp.presentation.savedscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.MainRepository
import com.example.weatherapp.data.locale.db.WeatherItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    
    private val ioScope = CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    
    val whetherItemsFlow = mainRepository.observeWeatherItems()
    
    fun deleteWeatherItem(item: WeatherItem) {
        ioScope.launch {
            mainRepository.deleteWeatherItem(item)
        }
    }
}