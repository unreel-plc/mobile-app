package com.unreel.unreel.ui.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unreel.unreel.ui.components.ErrorDialog
import com.unreel.unreel.ui.components.LoadingDialog
import com.unreel.unreel.core.navigation.NavScreenGraph
import com.unreel.unreel.ui.feature.auth.login.google.GoogleSignInButton
import com.unreel.unreel.ui.feature.auth.login.google.GoogleAuthProvider
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
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

    val googleAuthProvider = koinInject<GoogleAuthProvider>()
    val coroutineScope = rememberCoroutineScope()

    val darkNavyBlue = Color(0xFF1A2332)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkNavyBlue)
    ) {
        Icon(
            imageVector = Icons.Filled.DarkMode,
            contentDescription = "Dark Mode",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 50.dp)
                .size(24.dp)
                .background(
                    color = Color(0xFF374151),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(6.dp)
        )
        
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .offset(y = (85).dp, x = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.BookmarkBorder,
                contentDescription = "Bookmark",
                tint = Color.White,
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color(0xFF374151),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(12.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFF8B5CF6)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Welcome to SparkReel",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Sign in with your Google account to continue",
                color = Color(0xFF9CA3AF),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2A3441),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                val googleAuthProvider = googleAuthProvider.getUiProvider()
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val googleUser = googleAuthProvider.signIn()
                            val idToken = googleUser?.token
                            if (idToken != null) {
                                onAction(Action.OnGoogleLogin(idToken))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF374151)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "G",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF6366F1),
                                            Color(0xFF8B5CF6)
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .wrapContentSize(Alignment.Center)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Continue with Google",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}