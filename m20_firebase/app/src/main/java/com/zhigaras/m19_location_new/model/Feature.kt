package com.zhigaras.m19_location_new.model

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)