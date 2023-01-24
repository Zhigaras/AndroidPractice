package com.example.weatherapp.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weatherapp.presentation.Destination
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
            Row(
                Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
            modifier = Modifier.weight(1f)
        )
        if (!selected) {
            Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Preview
@Composable
fun BottomTabRowPreview() {
    
    BottomTabRow(bottomTabList, {}, Search, true)
    
}
