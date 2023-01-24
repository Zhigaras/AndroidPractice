package com.example.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.presentation.compose.BottomTabRow
import com.example.weatherapp.presentation.detailsscreen.DetailsScreen
import com.example.weatherapp.presentation.savedscreen.SavedScreen
import com.example.weatherapp.presentation.searchscreen.SearchScreen
import com.example.weatherapp.presentation.theme.WeatherAppComposableTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WeatherAppComposableTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApplication()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApplication() {
    
    var bottomBarState by rememberSaveable { (mutableStateOf(true)) }
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = bottomTabList.find { it.route == currentDestination?.route } ?: Search
    
    bottomBarState = when (currentDestination?.route) {
        Details.routeWithArgs -> false
        else -> true
    }
    
    Scaffold(
        bottomBar = {
            BottomTabRow(
                allScreens = bottomTabList,
                onTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = currentScreen,
                bottomBarState = bottomBarState
            )
        }
    ) { innerPadding ->
        SetUpNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SetUpNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
    ) {
    NavHost(
        navController = navController,
        startDestination = Search.route,
        modifier = modifier
    ) {
        composable(route = Search.route) {
            SearchScreen(
                startTransition = { cityName ->
                    navController.navigateSingleTopTo("${Details.route}/$cityName")
                }
            )
        }
        composable(
            route = Details.routeWithArgs,
            arguments = Details.arguments
        ) {navBackStackEntry ->
            val cityName = navBackStackEntry.arguments?.getString(Details.cityName)
            DetailsScreen(cityName!!)
        }
        composable(route = Saved.route) {
            SavedScreen(
                startTransition = { cityName ->
                    navController.navigateSingleTopTo("${Details.route}/$cityName")
                }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }