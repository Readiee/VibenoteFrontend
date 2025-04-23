package com.vbteam.vibenote.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun FancyTabs(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        FancyIndicator(tabPositions, selectedTabIndex)
    }

    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = indicator,
        containerColor = MaterialTheme.colorScheme.surface,
        divider = {},

        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(shape = RoundedCornerShape(100.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(100.dp))

    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.height(40.dp),
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (selectedTabIndex == index)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}

@Composable
fun FancyIndicator(
    tabPositions: List<TabPosition>,
    selectedTabIndex: Int
) {
    val transition = updateTransition(selectedTabIndex, label = "Tab indicator")

    val indicatorLeft by transition.animateDp(label = "Indicator Left") {
        tabPositions[it].left
    }
    val indicatorRight by transition.animateDp(label = "Indicator Right") {
        tabPositions[it].right
    }

    Box(
        Modifier
            .wrapContentSize(Alignment.Center)
            .tabIndicatorOffset(tabPositions[selectedTabIndex])
            .zIndex(-1f)
            .clip(RoundedCornerShape(200.dp))
            .fillMaxHeight(1f)
            .background(MaterialTheme.colorScheme.primary)
    )
}
