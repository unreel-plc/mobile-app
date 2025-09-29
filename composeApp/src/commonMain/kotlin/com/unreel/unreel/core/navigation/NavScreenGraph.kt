package com.unreel.unreel.core.navigation
sealed class NavScreenGraph(val route: String) {
    object LoginScreen : NavScreenGraph("LoginScreen"){
        const val argument = "LoginScreen?phoneNumber={phoneNumber}"
        const val argument0 :String = "phoneNumber"
    }
    object DownloadDetailScreen : NavScreenGraph("DownloadDetailScreen"){
        const val argument = "DownloadDetailScreen/{id}"
        const val argument0 :String = "id"
    }
    object MainScreen : NavScreenGraph("MainScreen")
    object HomeScreen : NavScreenGraph("HomeScreen")
}