package edu.skillbox.m18_permissions.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "photoUri")
    val photoUri: String,
    @ColumnInfo(name = "dateOfPhoto")
    val dateOfPhoto: String
)
