package com.zhigaras.recyclerviewadvanced.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiReply(
    @Json(name = "info")
    val info: Info,
    @Json(name = "results")
    val personages: List<Personage>
)