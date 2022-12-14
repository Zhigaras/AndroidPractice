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
    
    init {
        getRoversPhoto()
    }
    
    private val _roversPhotoFlow = MutableStateFlow<List<MarsRoversPhoto>>(emptyList())
    val roversPhotoFlow = _roversPhotoFlow.asStateFlow()
    
    fun getRoversPhoto() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getMarsRoversPhotoUseCase.fetchMarsRoversPhoto()
            }.fold(
                onSuccess = { _roversPhotoFlow.value = it },
                onFailure = { Log.d("MainViewModel",it.message ?: "")}
            )
        }
    }
}