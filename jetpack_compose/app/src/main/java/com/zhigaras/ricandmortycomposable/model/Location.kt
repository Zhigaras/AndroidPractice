package com.zhigaras.ricandmortycomposable.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "created")
    val created: String?,
    @Json(name = "dimension")
    val dimension: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String,
    @Json(name = "residents")
    val residents: List<String>?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val url: String
) {
    val gotDimension: String?
        get() = if (dimension == "unknown") "Unknown dimension" else dimension
    
    val gotType: String?
        get() = if (type == "unknown") "Unknown type" else type
}