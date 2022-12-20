package edu.skillbox.m18_permissions.presentation

import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.skillbox.m18_permissions.data.database.PhotoDao
import edu.skillbox.m18_permissions.data.database.PhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

class CameraViewModel(private val photoDao: PhotoDao) : ViewModel() {

    private var _lastPhotoFlow = MutableStateFlow<PhotoModel?>(null)
    val lastPhotoFlow = _lastPhotoFlow.asStateFlow()
    init {
        getLastPhoto()
    }
    
    fun savePhoto(photo: PhotoModel) {
        viewModelScope.launch(IO) {
            photoDao.insertPhoto(photo)
            getLastPhoto()
        }
    }
    
    private fun getLastPhoto() {
        viewModelScope.launch {
            _lastPhotoFlow.value = photoDao.showLastPhoto()
        }
    }
}