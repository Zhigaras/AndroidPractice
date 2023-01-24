package com.example.weatherapp.data.locale

import com.example.weatherapp.data.locale.db.WeatherDao
import com.example.weatherapp.data.locale.db.WeatherItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocaleRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val dataStoreManager: DataStorageManager
) {
    suspend fun saveWeatherItem(weatherItem: WeatherItem) {
        weatherDao.insertWeatherItem(weatherItem)
    }
    
    suspend fun findWeatherItemByCityName(request: String): WeatherItem? {
        return weatherDao.findWeatherItemByCityName(request)
    }
    
    suspend fun deleteWeatherItem(weatherItem: WeatherItem) {
        weatherDao.deleteWeatherItem(weatherItem)
    }
    
    fun observeWeatherItems(): Flow<List<WeatherItem>> {
        return weatherDao.observeWeatherItems()
    }
    
    suspend fun saveRequestHistoryItem(item:String) {
        dataStoreManager.saveItemToPrefs(item)
    }
    
    suspend fun deleteRequestHistoryItem(item: String) {
        dataStoreManager.deleteItemFromPrefs(item)
    }
    
    suspend fun clearRequestHistory() {
        dataStoreManager.clearPrefs()
    }
    
    fun observeRequestHistory(): Flow<List<String>> {
        return dataStoreManager.observePrefs().map {
            it.toMutablePreferences()
                .asMap()
                .mapKeys { (k, _) -> k.name }
                .mapValues { (_, v) -> v.toString().toLongOrNull() }
                .toList()
                .sortedBy { (_, v) -> v }
                .reversed()
                .map { (k, _) -> k }
        }
    }
}