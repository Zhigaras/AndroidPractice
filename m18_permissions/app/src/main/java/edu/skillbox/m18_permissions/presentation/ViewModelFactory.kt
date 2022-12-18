package edu.skillbox.m18_permissions.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.skillbox.m18_permissions.data.database.App
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val app: App
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            
            GalleryViewModel::class.java -> GalleryViewModel(app.db.photoDao())
            
//            CameraViewModel::class.java -> CameraViewModel
            
            else -> throw java.lang.IllegalArgumentException("Unknown class name")
        }
        return viewModel as T
    }
}