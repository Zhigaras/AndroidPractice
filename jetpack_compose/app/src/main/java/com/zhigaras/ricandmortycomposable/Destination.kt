package com.zhigaras.ricandmortycomposable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
    val icon: ImageVector
}

object PersonageList : Destination {
    override val route = "Personages List"
    override val icon = Icons.Filled.Person
    
}

object Details : Destination {
    override val route = "Details"
    override val icon = Icons.Filled.Person
    const val personageIdArg = "personage_id"
    val routeWithArgs = "$route/{$personageIdArg}"
    val arguments = listOf(
        navArgument(personageIdArg) { type = NavType.IntType }
    )
}

object LocationsList : Destination {
    override val route = "Locations List"
    override val icon = Icons.Filled.LocationOn
}