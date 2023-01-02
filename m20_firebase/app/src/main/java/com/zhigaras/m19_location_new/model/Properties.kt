package com.zhigaras.m19_location_new.model

data class Properties(
    val addressLine1: String,
    val addressLine2: String,
    val categories: List<String>,
    val city: String,
    val country: String,
    val countryCode: String,
    val county: String?,
    val datasource: Datasource,
    val details: List<String>,
    val distance: Int,
    val formatted: String,
    val lat: Double,
    val lon: Double,
    val name: String?,
    val placeId: String,
    val postcode: String,
    val state: String,
    val street: String,
    val suburb: String?
)