package com.example.weatherapp.data.remote.responses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "code")
    val code: Int,
    @Json(name = "icon")
    val icon: String,
    @Json(name = "text")
    val text: String
)