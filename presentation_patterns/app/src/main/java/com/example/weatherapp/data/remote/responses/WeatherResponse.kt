package com.example.weatherapp.data.remote.responses


import com.example.weatherapp.data.locale.db.WeatherItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "current")
    val current: Current,
    @Json(name = "location")
    val location: Location
) {
    
    fun toWeatherItem(): WeatherItem {
        val updateDate = current.lastUpdated.takeWhile { it != ' ' }
        return WeatherItem(
            id = location.name + updateDate,
            cityName = location.name,
            countryName = location.country,
            lastUpdatedEpoch = current.lastUpdatedEpoch,
            lastUpdated = updateDate,
            temp = current.tempC,
            condition = current.condition.text,
            conditionIcon = current.condition.icon,
            windKmh = current.windKph
        )
    }
}