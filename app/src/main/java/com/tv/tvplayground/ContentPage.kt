package com.tv.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClassicCard
import androidx.tv.material3.Text

val ParentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp)

@Immutable
data class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
)

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}

data class Item(
    val name: String,
    val desc: String
)

val tidalItems = listOf(
    Item("Tidal Item 1", "Tidal desc 1"),
    Item("Tidal Item 2", "Tidal desc 2"),
    Item("Tidal Item 3", "Tidal desc 3")
)

val spotifyItems = listOf(
    Item("Spotify Item 1", "Spotify desc 1"),
    Item("Spotify Item 2", "Spotify desc 2"),
    Item("Spotify Item 3", "Spotify desc 3")
)


val deezerItems = listOf(
    Item("Deezer Item 1", "Deezer desc 1"),
    Item("Deezer Item 2", "Deezer desc 2"),
    Item("Deezer Item 3", "Deezer desc 3")
)

@Composable
fun ContentPage(
    rowItems: List<Item>,
    modifier: Modifier = Modifier,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
) {
    Column {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = startPadding, end = endPadding)
        ) {
            items(
                count = rowItems.size,
                contentType = { it.javaClass }
            ) { position ->
                Card(rowItems[position])
            }
        }
        Spacer(Modifier.height(30.dp))
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = startPadding, end = endPadding)
        ) {
            items(
                count = rowItems.size,
                contentType = { it.javaClass }
            ) { position ->
                Card(rowItems[position])
            }
        }
    }
}

@Composable
private fun Card(
    item: Item,
    modifier: Modifier = Modifier
) {
    ClassicCard(
        colors = CardDefaults.colors(
            containerColor = Color.Black,
            contentColor = Color.Black,
            focusedContainerColor = Color.Black,
            focusedContentColor = Color.Black,
            pressedContainerColor = Color.Black,
            pressedContentColor = Color.Black
        ),
        modifier = modifier.size(160.dp, 230.dp),
        onClick = { },
        image = { },
        title = {
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(item.name, color = Color.White)
            }
        },
        subtitle = {
            Row(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(item.desc, color = Color.White)
            }
        }
    )
}
