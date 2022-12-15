package edu.skillbox.m17_recyclerview.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.skillbox.m17_recyclerview.domain.GetMarsRoversPhotoUseCase
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhotosList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMarsRoversPhotoUseCase: GetMarsRoversPhotoUseCase
) : ViewModel() {
    
    private val _roversPhotoFlow = MutableStateFlow<List<MarsRoversPhoto>>(emptyList())
    val roversPhotoFlow = _roversPhotoFlow.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    init {
        getRoversPhoto()
    }
    
    private fun getRoversPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                getMarsRoversPhotoUseCase.fetchMarsRoversPhoto()
            }.fold(
                onSuccess = { _roversPhotoFlow.value = it },
                onFailure = { Log.d("MainViewModel",it.message ?: "")}
            )
            _isLoading.value = false
        }
    }
}