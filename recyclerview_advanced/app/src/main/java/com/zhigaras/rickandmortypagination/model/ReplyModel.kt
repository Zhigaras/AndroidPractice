package com.zhigaras.rickandmortypagination.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReplyModel(
    @Json(name = "info")
    val info: Info,
    @Json(name = "results")
    val personages: List<Personage>
)