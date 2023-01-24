package com.example.weatherapp.data.locale.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_items")
data class WeatherItem(
    @PrimaryKey
    val id: String = "",
    val cityName: String = "",
    val countryName: String = "",
    val lastUpdatedEpoch: Int = 0,
    val lastUpdated: String = "",
    val temp: Double = 0.0,
    val condition: String = "",
    val conditionIcon: String = "",
    val windKmh: Double = 0.0
)
