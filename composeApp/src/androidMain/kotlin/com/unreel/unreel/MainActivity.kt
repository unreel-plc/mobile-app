package com.unreel.unreel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.core.navigation.NavScreenGraph
import com.unreel.unreel.networks.repository.IntentKeywords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

val START_DESTINATION = NavScreenGraph.LoginScreen.route
val LOGGED_IN_START_DESTINATION = NavScreenGraph.LoginScreen.route
val LOGGED_OUT_START_DESTINATION = NavScreenGraph.LoginScreen.route

class MainActivity : ComponentActivity() {
    private val offlineRepository: OfflineRepository by inject()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            false
        }
        enableEdgeToEdge()

        val accessToken: Flow<String?> = offlineRepository.getAccessToken()

        setContent {
            App(
                getStartDestination(
                    accessToken,
                )
            )
        }
    }

    private fun getStartDestination(
        accessToken: Flow<String?>,
    ): String {
        if (intent.hasExtra(IntentKeywords.StartDestination.name)) {
            if (intent.getStringExtra(IntentKeywords.StartDestination.name)!! == IntentKeywords.SESSION_EXPIRED.name) {
                Toast.makeText(this, "Your Session Expired, Please Login Again", Toast.LENGTH_SHORT)
                    .show()

                return NavScreenGraph.LoginScreen.route
            }

            return intent.getStringExtra(IntentKeywords.StartDestination.name)!!
        }

        return runBlocking {
            val accessTokenValue = accessToken.first()

            return@runBlocking when {
                !accessTokenValue.isNullOrEmpty() -> {
                    LOGGED_IN_START_DESTINATION
                }

                else -> {
                    START_DESTINATION
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}