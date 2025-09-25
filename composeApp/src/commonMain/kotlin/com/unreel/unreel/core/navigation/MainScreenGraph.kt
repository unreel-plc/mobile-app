package com.unreel.unreel.core.navigation

sealed class MainScreenGraph(val route: String) {
    object HomeScreen : MainScreenGraph("HomeScreen")
    object RedemptionHistoryScreen : MainScreenGraph("HistoryScreen")
    object AccountScreen : MainScreenGraph("AccountScreen")
}