package com.unreel.unreel

import androidx.compose.runtime.*
import com.unreel.unreel.ui.theme.unreelTheme
import com.unreel.unreel.core.navigation.ApplicationNavController
import com.unreel.unreel.core.navigation.NavScreenGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App(startDestination: String? = null) {
    unreelTheme {
        KoinContext {
            ApplicationNavController(
                startDestination = startDestination ?: NavScreenGraph.LoginScreen.route,
            )
        }
    }
}