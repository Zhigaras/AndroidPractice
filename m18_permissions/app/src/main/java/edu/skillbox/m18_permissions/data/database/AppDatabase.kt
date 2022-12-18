package edu.skillbox.m18_permissions.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun photoDao() : PhotoDao
}