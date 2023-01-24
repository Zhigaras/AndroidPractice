package com.example.weatherapp.presentation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.weatherapp.R

interface Destination {
    val icon: Int
    val route: String
}

object Search : Destination {
    override val icon = R.drawable.baseline_search_24
    override val route = "Search"
}

object Saved : Destination {
    override val icon = R.drawable.baseline_location_city_24
    override val route = "Saved"
}

object Details : Destination {
    override val icon = R.drawable.baseline_wb_sunny_24
    override val route = "Details"
    const val cityName = "cityName"
    val routeWithArgs = "$route/{$cityName}"
    val arguments = listOf(
        navArgument(cityName) { type = NavType.StringType }
    )
}

val bottomTabList = listOf(Search, Saved)