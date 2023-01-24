package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.responses.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = "b419f433edad4783b8c220317232101"
    }
    
    @Headers("key: $API_KEY")
    @GET("current.json")
    suspend fun getWeather(
        @Query("q") city: String
    ): Response<WeatherResponse>
}