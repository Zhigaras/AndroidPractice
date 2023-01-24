package com.example.weatherapp.data.locale.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherItem::class], version = 1, exportSchema = false)
abstract class WeatherItemDatabase : RoomDatabase(){

    abstract fun getWeatherDao(): WeatherDao
    
    companion object {
        const val DATABASE_NAME = "weather_item_database"
    }
}