package com.example.weatherapp.data

import com.example.weatherapp.data.locale.LocaleRepository
import com.example.weatherapp.data.locale.db.WeatherItem
import com.example.weatherapp.data.remote.RemoteRepository
import com.example.weatherapp.domain.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localeRepository: LocaleRepository
) {

    suspend fun getWeatherFromRemote(city: String): ApiResult<WeatherItem> {
        return remoteRepository.getWeather(city)
    }
    
    suspend fun saveWeatherItem(weatherItem: WeatherItem) {
        localeRepository.saveWeatherItem(weatherItem)
    }
    
    suspend fun findWeatherItemByCityName(request: String): WeatherItem? {
        return localeRepository.findWeatherItemByCityName(request)
    }
    
    suspend fun deleteWeatherItem(weatherItem: WeatherItem) {
        localeRepository.deleteWeatherItem(weatherItem)
    }

    fun observeWeatherItems(): Flow<List<WeatherItem>> {
        return localeRepository.observeWeatherItems()
    }
    
    suspend fun saveRequestHistoryItem(item:String) {
        localeRepository.saveRequestHistoryItem(item)
    }
    
    suspend fun deleteRequestHistoryItem(item: String) {
        localeRepository.deleteRequestHistoryItem(item)
    }
    
    suspend fun clearRequestHistory() {
        localeRepository.clearRequestHistory()
    }
    
    fun observeRequestHistory(): Flow<List<String>> {
        return localeRepository.observeRequestHistory()
    }
}