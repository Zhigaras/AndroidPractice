package edu.skillbox.m18_permissions.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    
    @Insert
    suspend fun insertPhoto(photo: PhotoModel)
    
    @Query("SELECT * FROM photos ORDER BY id DESC")
    suspend fun showAllPhotos(): List<PhotoModel>
    
    @Query("SELECT * FROM photos ORDER BY id DESC LIMIT 1")
    suspend fun showLastPhoto(): PhotoModel
}