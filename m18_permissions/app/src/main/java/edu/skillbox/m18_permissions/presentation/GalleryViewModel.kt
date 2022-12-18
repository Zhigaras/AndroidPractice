package edu.skillbox.m18_permissions.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.skillbox.m18_permissions.data.Repository
import edu.skillbox.m18_permissions.data.database.PhotoDao
import edu.skillbox.m18_permissions.data.database.PhotoModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    val photoDao: PhotoDao
) : ViewModel() {





}