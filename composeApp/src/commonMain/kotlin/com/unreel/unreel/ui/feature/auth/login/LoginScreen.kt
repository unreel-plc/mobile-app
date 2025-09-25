package com.unreel.unreel.ui.feature.auth.login

import androidx.compose.foundation.clickable
import kotlinx.serialization.json.Json
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unreel.unreel.ui.components.ErrorDialog
import com.unreel.unreel.ui.components.LoadingDialog
import com.unreel.unreel.core.navigation.NavScreenGraph
import com.unreel.unreel.ui.feature.auth.login.google.GoogleSignInButton
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    navController: NavController = rememberNavController(),
) {
    val viewModel = koinViewModel<LoginViewModel>()
    val state by viewModel.collectAsState()
    viewModel.collectSideEffect {
        when (it) {
            is Event.NavigateToDashboard -> {
                navController.navigate(NavScreenGraph.MainScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
                viewModel.onAction(Action.OnStopLoading)
            }
        }
    }

    LoginScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

expect fun platformBase64Decode(input: String): ByteArray

fun decodeJwtPayload(idToken: String): Map<String, String>? {
    val parts = idToken.split(".")
    if (parts.size != 3) return null

    val payloadBase64 = parts[1]
    val payloadBytes = platformBase64Decode(payloadBase64) // implement per platform
    val payloadString = payloadBytes.decodeToString()

    val json = Json.parseToJsonElement(payloadString).jsonObject
    return json.mapValues { it.value.jsonPrimitive.content }
}


@Composable
fun LoginScreenContent(
    state: State = State(),
    onAction: (Action) -> Unit = {},
) {
    if (state.loadingState.isLoading) {
        LoadingDialog(message = state.loadingState.message!!)
    }

    if (state.error != null) {
        ErrorDialog(message = state.error, onConfirm = { onAction(Action.OnDismissError) })
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Click Me", modifier = Modifier.clickable {
                    onAction(Action.OnSendHello)
                })
            if (state.message != null) {
                Text(
                    "Message is ${state.message}"
                )
            }

            if (state.user != null) {
                Text(
                    "${state.user.id}"
                )
            }

            GoogleSignInButton(onGoogleSignInResult = { googleUser ->
                val idToken = googleUser?.token
                if (idToken != null) {
                    onAction(Action.OnGoogleLogin(idToken))
                }
            })
        }
    }
}