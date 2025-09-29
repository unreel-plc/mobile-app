package com.unreel.unreel.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.unreel.unreel.ui.feature.auth.login.LoginScreen
import com.unreel.unreel.ui.feature.auth.login.LoginViewModel
import com.unreel.unreel.ui.feature.main.home.HomeScreen
import com.unreel.unreel.ui.feature.main.home.download_detail.DownloadDetailScreen
import com.unreel.unreel.ui.feature.main.home.download_detail.DownloadDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ApplicationNavController(
    startDestination: String,
    onStart: (NavHostController) -> Unit = {}
) {

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        onStart(navController)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            composable(
                NavScreenGraph.MainScreen.route
            ) {
                HomeScreen(
                    navController
                )
            }

            composable(
                NavScreenGraph.LoginScreen.argument,
                arguments = listOf(
                    navArgument(NavScreenGraph.LoginScreen.argument0) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null

                    }
                )
            ) {
                val phoneNumber =
                    it.arguments?.getString(NavScreenGraph.LoginScreen.argument0) ?: ""
                val viewModel = koinViewModel<LoginViewModel>()
                LaunchedEffect(phoneNumber) {
                    if (phoneNumber.isNotEmpty()) {
                        viewModel.setPhoneNumber(phoneNumber)
                    }
                }
                LoginScreen(navController)
            }

            composable(
                NavScreenGraph.DownloadDetailScreen.argument,
                arguments = listOf(
                    navArgument(NavScreenGraph.DownloadDetailScreen.argument0) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                val id =
                    it.arguments?.getString(NavScreenGraph.DownloadDetailScreen.argument0) ?: ""
                val viewModel = koinViewModel<DownloadDetailViewModel>()
                LaunchedEffect(id) {
                    if (id.isNotEmpty()) {
                        viewModel.setDownloadId(id)
                    }
                }
                DownloadDetailScreen(navController, viewModel)
            }


        }
    }

}