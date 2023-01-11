package com.zhigaras.ricandmortycomposable.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.zhigaras.ricandmortycomposable.R

@JsonClass(generateAdapter = true)
data class Personage(
    @Json(name = "created")
    val created: String,
    @Json(name = "episode")
    val episode: List<String>,
    @Json(name = "gender")
    val gender: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "location")
    val location: Location,
    @Json(name = "name")
    val name: String,
    @Json(name = "origin")
    val origin: Origin,
    @Json(name = "species")
    val species: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "url")
    val url: String,
    val statusMarker: Int = when (status.lowercase()) {
        "alive" -> R.drawable.status_alive
        "dead" -> R.drawable.status_dead
        else -> R.drawable.status_unknown
    }
)