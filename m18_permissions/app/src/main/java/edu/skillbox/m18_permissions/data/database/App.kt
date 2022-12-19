package edu.skillbox.m18_permissions.data.database

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp


class App: Application() {
    
    lateinit var db: AppDatabase
    
    override fun onCreate() {
        super.onCreate()
        
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "photoDb"
        ).build()
    }
}