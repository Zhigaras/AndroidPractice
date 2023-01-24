package com.example.weatherapp.data.locale.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherItem(weatherItem: WeatherItem)
    
    @Query("SELECT * FROM weather_items WHERE cityName LIKE '%' || :request || '%' ORDER BY lastUpdatedEpoch DESC LIMIT 1")
    suspend fun findWeatherItemByCityName(request: String): WeatherItem?
    
    @Delete()
    suspend fun deleteWeatherItem(weatherItem: WeatherItem)
    
    @Query("SELECT * FROM weather_items ORDER BY lastUpdatedEpoch DESC")
    fun observeWeatherItems(): Flow<List<WeatherItem>>
}