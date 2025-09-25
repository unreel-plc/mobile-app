package com.unreel.unreel.ui.feature.auth.login.google

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import unreel.composeapp.generated.resources.Res
import unreel.composeapp.generated.resources.android_neutral_rd_na
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

interface GoogleButtonClick {
    fun onSignInClicked()

    fun onSignOutClicked()
}

@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onGoogleSignInResult: (GoogleAccount?) -> Unit,
) {
    val googleAuthProvider = koinInject<GoogleAuthProvider>()
    val googleAuthUiProvider = googleAuthProvider.getUiProvider()
    val coroutineScope = rememberCoroutineScope()
    val uiContainerScope =
        remember {
            object : GoogleButtonClick {
                override fun onSignInClicked() {
                    coroutineScope.launch {
                        val googleUser = googleAuthUiProvider.signIn()
                        onGoogleSignInResult(googleUser)
                    }
                }


                override fun onSignOutClicked() {
                    coroutineScope.launch {
                        googleAuthProvider.signOut()
                    }
                }
            }
        }


    Column (
        modifier = Modifier.clickable {
            uiContainerScope.onSignInClicked()
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SocialIcon(
            iconResId = Res.drawable.android_neutral_rd_na,
            contentDescription = "Google Login",
            onClick = {
                uiContainerScope.onSignInClicked()
            },
        )
    }
}


@Composable
fun SocialIcon(
    iconResId: DrawableResource,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(45.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(resource = iconResId),
            contentDescription = contentDescription,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize().clickable(onClick = onClick),
        )
    }
}
