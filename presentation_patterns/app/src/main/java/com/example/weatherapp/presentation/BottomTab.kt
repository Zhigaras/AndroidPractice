package com.example.weatherapp.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weatherapp.presentation.Destination
import com.example.weatherapp.presentation.Saved
import com.example.weatherapp.presentation.Search
import com.example.weatherapp.presentation.bottomTabList

@Composable
fun BottomTabRow(
    allScreens: List<Destination>,
    onTabSelected: (Destination) -> Unit = {},
    currentScreen: Destination,
    bottomBarState: Boolean
) {
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Surface(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        ) {
            TabRow(
                selectedTabIndex = 0,
                Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                indicator = { tabPosition ->
                    TabIndicator(tabPositions = tabPosition, tabPage = currentScreen)
                }
            ) {
                allScreens.forEach { screen ->
                    BottomTab(
                        text = screen.route,
                        icon = screen.icon,
                        onSelected = { onTabSelected(screen) },
                        selected = currentScreen == screen
                    )
                }
            }
        }
    }
}

@Composable
fun BottomTab(
    text: String,
    icon: Int,
    onSelected: () -> Unit,
    selected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(56.dp)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
            Text(text = text, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun TabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: Destination
) {
    val transition = updateTransition(
        tabPage,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (Search isTransitioningTo Saved) {
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { page ->
        tabPositions[page.pageNumber].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (Search isTransitioningTo Saved) {
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.pageNumber].right
    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                RoundedCornerShape(4.dp)
            )
    )
}

@Preview
@Composable
fun BottomTabRowPreview() {
    
    BottomTabRow(bottomTabList, {}, Search, true)
    
}
