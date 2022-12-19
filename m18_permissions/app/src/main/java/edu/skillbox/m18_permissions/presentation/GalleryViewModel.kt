package edu.skillbox.m18_permissions.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.skillbox.m18_permissions.data.database.App
import edu.skillbox.m18_permissions.data.database.PhotoDao
import edu.skillbox.m18_permissions.data.database.PhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GalleryViewModel(private val photoDao: PhotoDao) : ViewModel() {
    
    private val _photoFlow = MutableStateFlow<List<PhotoModel>>(emptyList())
    val photoFlow = _photoFlow.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    init {
        showPhotos()
    }
    
    private fun showPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                photoDao.showAllPhotos()
            }.fold(
                onSuccess = { _photoFlow.value = it },
                onFailure = { Log.d("MainViewModel", it.message ?: "") }
            )
            _isLoading.value = false
        }
    }
}