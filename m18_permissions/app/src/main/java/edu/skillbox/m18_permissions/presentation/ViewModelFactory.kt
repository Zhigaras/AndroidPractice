package edu.skillbox.m18_permissions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.skillbox.m18_permissions.data.database.PhotoDao

class ViewModelFactory (private val photoDao: PhotoDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            
            GalleryViewModel::class.java -> GalleryViewModel(photoDao)
            
            CameraViewModel::class.java -> CameraViewModel(photoDao)
            
            else -> throw java.lang.IllegalArgumentException("Unknown class name")
        }
        return viewModel as T
    }
}