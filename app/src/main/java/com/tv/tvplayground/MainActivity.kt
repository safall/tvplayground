package com.tv.tvplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Icon
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.tv.playground.ContentPage
import com.tv.playground.deezerItems
import com.tv.playground.spotifyItems
import com.tv.playground.tidalItems
import com.tv.tvplayground.ui.theme.TvplaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TvplaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    Drawer()
                }
            }
        }
    }
}

private enum class NavMenuItem {
    TIDAL, SPOTIFY, DEEZER
}

private fun NavMenuItem.getIcon(): ImageVector {
    return when (this) {
        NavMenuItem.TIDAL -> Icons.Default.Home
        NavMenuItem.SPOTIFY -> Icons.Default.Favorite
        NavMenuItem.DEEZER -> Icons.Default.Settings
    }
}

@Composable
private fun Drawer(
    items: List<NavMenuItem> = listOf(
        NavMenuItem.TIDAL,
        NavMenuItem.SPOTIFY,
        NavMenuItem.DEEZER
    )
) {
    // keep reference to focus state for each items
    val itemFocusState = List(size = items.size) { mutableStateOf(false) }
    val itemsFocusRequester = remember { List(size = items.size) { FocusRequester() } }
    var selectedIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()

    NavigationDrawer(
        drawerContent = {
            Column(
                Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(
                    space = 10.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused && !itemFocusState[index].value) {
                                    selectedIndex = index
                                    navController.navigate(item.name)
                                    itemFocusState[index].value = true
                                } else if (!focusState.isFocused && itemFocusState[index].value) {
                                    itemFocusState[index].value = false
                                }
                            }
                            .focusRequester(itemsFocusRequester[index])
                            .focusable(),
                        selected = selectedIndex == index,
                        onClick = {},
                        leadingContent = {
                            Icon(
                                imageVector = item.getIcon(),
                                contentDescription = null,
                            )
                        }
                    ) {
                        Text(item.name)
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavMenuItem.TIDAL.name,
            builder = {
                composable(NavMenuItem.TIDAL.name) {
                    ContentPage(
                        tidalItems,
                        Modifier.focusOnLeftExit(itemsFocusRequester[0])
                    )
                }
                composable(NavMenuItem.SPOTIFY.name) {
                    ContentPage(
                        spotifyItems,
                        Modifier.focusOnLeftExit(itemsFocusRequester[1])
                    )
                }
                composable(NavMenuItem.DEEZER.name) {
                    ContentPage(
                        deezerItems,
                        Modifier.focusOnLeftExit(itemsFocusRequester[2])
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.focusOnLeftExit(focusRequester: FocusRequester): Modifier {
    return focusProperties {
        exit = { focusDirection ->
            when (focusDirection) {
                FocusDirection.Left -> focusRequester
                else -> FocusRequester.Default
            }
        }
    }
}
