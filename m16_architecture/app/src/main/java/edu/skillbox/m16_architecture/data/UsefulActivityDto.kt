package edu.skillbox.m16_architecture.data

import edu.skillbox.m16_architecture.entity.UsefulActivity

data class UsefulActivityDto(
    override val accessibility: Double,
    override val activity: String,
    override val key: String,
    override val link: String,
    override val participants: Int,
    override val price: Double,
    override val type: String
) : UsefulActivity