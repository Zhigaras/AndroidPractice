package edu.skillbox.m18_permissions.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "photos")
@Parcelize
data class PhotoModel(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "photoUri")
    val photoUri: String,
    @ColumnInfo(name = "dateOfPhoto")
    val dateOfPhoto: String
) : Parcelable
