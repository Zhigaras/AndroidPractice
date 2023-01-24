package com.example.weatherapp.presentation.detailsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.MainRepository
import com.example.weatherapp.data.locale.db.WeatherItem
import com.example.weatherapp.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    
    private val ioScope = CoroutineScope(viewModelScope.coroutineContext + Dispatchers.IO)
    
    private val _detailsFlow = MutableStateFlow<ApiResult<WeatherItem>>(ApiResult.Loading())
    val detailsFlow = _detailsFlow.asStateFlow()
    
    fun getWeather(cityName: String) {
        ioScope.launch {
            _detailsFlow.value = ApiResult.Loading()
            val dbReply = mainRepository.findWeatherItemByCityName(cityName)
            if (dbReply != null) {
                _detailsFlow.value = ApiResult.Success(dbReply)
                Log.d("XXX", "loaded from db")
            } else {
                val remoteResponse = mainRepository.getWeatherFromRemote(cityName)
                _detailsFlow.value = remoteResponse
                Log.d("XXX", "loaded from remote")
                if (remoteResponse is ApiResult.Success) {
                    if (remoteResponse.data != null)
                        mainRepository.saveWeatherItem(remoteResponse.data)
                }
            }
        }
    }
}