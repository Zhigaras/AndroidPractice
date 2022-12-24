package com.zhigaras.m19_location_new

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhigaras.m19_location_new.model.Feature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MapsViewModel: ViewModel() {
    
    private val remoteRepository = RemoteRepository().placesApi
    
    private var _placesFlow = MutableStateFlow<List<Feature>>(emptyList())
    val placesFlow = _placesFlow.asStateFlow()
    
    private var _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()
    
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    init {
        Log.d("viewModel", "init viewModel")
    }
    
    fun getPlaces(lon: Double, lat: Double) {
        Log.d("Debug", "start getting places")
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                delay(1_000)
                remoteRepository.findPlaces("proximity:$lon,$lat")
            }.fold(
                onSuccess = { _placesFlow.value = it.features },
                onFailure = { _errorChannel.send("Download error")}
            )
            _isLoading.value = false


//            isLoading = true
//            val response = remoteRepository.findPlaces("proximity:$lon,$lat")
//            if (response.isSuccessful) {
//                try {
//                    response.body()?.let { _placesChannel.send(it.features) }
//                } catch (e: Throwable) {
//                    _errorChannel.send(e.message.toString())
//                }
//            } else {
//                _errorChannel.send("Download error")
//            }
//            isLoading = false
        }
    }
}