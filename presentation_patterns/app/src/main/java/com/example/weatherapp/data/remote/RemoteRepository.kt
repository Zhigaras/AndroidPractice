package com.example.weatherapp.data.remote

import com.example.weatherapp.data.locale.db.WeatherItem
import com.example.weatherapp.domain.ApiResult
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val weatherApi: WeatherApi
): BaseRepo() {
    
    suspend fun getWeather(city: String): ApiResult<WeatherItem> {
        return safeApiCall { weatherApi.getWeather(city) }
    }
}