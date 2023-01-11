package com.zhigaras.ricandmortycomposable.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationsApiReply(
    @Json(name = "info")
    val info: Info,
    @Json(name = "results")
    val locations: List<Location>
)