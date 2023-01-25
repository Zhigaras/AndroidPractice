package com.example.weatherapp.presentation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.weatherapp.R

interface Destination {
    val icon: Int
    val route: String
    val pageNumber: Int
}

object Search : Destination {
    override val icon = R.drawable.baseline_search_24
    override val route = "Search"
    override val pageNumber = 0
}

object Saved : Destination {
    override val icon = R.drawable.baseline_location_city_24
    override val route = "Saved"
    override val pageNumber = 1
}

object Details : Destination {
    override val icon = R.drawable.baseline_wb_sunny_24
    override val route = "Details"
    override val pageNumber = 2
    const val cityName = "cityName"
    val routeWithArgs = "$route/{$cityName}"
    val arguments = listOf(
        navArgument(cityName) { type = NavType.StringType }
    )
}

val bottomTabList = listOf(Search, Saved)