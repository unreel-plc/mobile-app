package com.unreel.unreel.ui.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.unreel.unreel.core.navigation.MainScreenGraph

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val to: String,
    val showTopbar: Boolean = false,
    val topBarTitle: String = "",
)

@Composable
fun getBottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Outlined.Home,
            unselectedIcon = Icons.Filled.Home,
            to = MainScreenGraph.HomeScreen.route,
        ),
        BottomNavigationItem(
            title = "History",
            selectedIcon = Icons.Outlined.History,
            unselectedIcon = Icons.Filled.History,
            to = MainScreenGraph.RedemptionHistoryScreen.route,
        ),
        BottomNavigationItem(
            title = "Account",
            selectedIcon = Icons.Outlined.AccountCircle,
            unselectedIcon = Icons.Filled.AccountCircle,
            to = MainScreenGraph.AccountScreen.route,
        ),
    )
}