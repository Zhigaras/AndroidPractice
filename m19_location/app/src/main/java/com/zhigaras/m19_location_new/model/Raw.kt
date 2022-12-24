package com.zhigaras.m19_location_new.model

data class Raw(
    val historic: String,
    val memorial: String?,
    val name: String?,
    val nameEn: String?,
    val osmId: Long,
    val osmType: String
)