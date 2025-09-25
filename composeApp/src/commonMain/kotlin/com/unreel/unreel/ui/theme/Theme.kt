package com.unreel.unreel.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val LightColorScheme = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onPrimary,
    secondary = light_secondary,
    onSecondary = light_onSecondary,
    tertiary = light_tertiary,
    onTertiary = light_onPrimary,
    error = light_error,
    onError = light_onError,
    background = light_background,
    onBackground = light_onBackground,
    surface = light_surface,
    onSurface = light_onSurface,
)
private val DarkColorScheme = darkColorScheme(
    /*   primary = dark_primary,
       onPrimary = dark_onPrimary,
       primaryContainer = dark_primaryContainer,
       onPrimaryContainer = dark_onPrimaryContainer,
       secondary = dark_secondary,
       onSecondary = dark_onSecondary,
       secondaryContainer = dark_secondaryContainer,
       onSecondaryContainer = dark_onSecondaryContainer,
       tertiary = dark_tertiary,
       onTertiary = dark_onTertiary,
       tertiaryContainer = dark_tertiaryContainer,
       onTertiaryContainer = dark_onTertiaryContainer,
       error = dark_error,
       errorContainer = dark_errorContainer,
       onError = dark_onError,
       onErrorContainer = dark_onErrorContainer,
       background = dark_background,
       onBackground = dark_onBackground,*/
)
@Composable
fun unreelTheme(
    showFullScreen: Boolean = false,
    darkTheme: Boolean = false, // Always force light theme
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Always use light color scheme regardless of system theme
    val colorScheme =  LightColorScheme

   // val view = LocalView.current

  /*  if (!view.isInEditMode && !showFullScreen) {
        SideEffect {
            val window = (view.context as Activity).window

            // Force light status bar
            window.statusBarColor = colorScheme.primary.toArgb()


            // Navigation bar with black transparent (50% opacity)
            //window.navigationBarColor = Color(0x80000000).toArgb()
            // Navigation bar with #F5F5F5 transparent (50% opacity)
            //window.navigationBarColor = Color(0x80F5F5F5).toArgb()
            window.navigationBarColor = Color(0xCCF5F5F5).toArgb()
            // Completely transparent navigation bar
            //window.navigationBarColor = Color.Transparent.toArgb()
            // window.navigationBarColor = colorScheme.background.toArgb()

            // Black transparent options
            // window.navigationBarColor = Color(0x40000000).toArgb() // 25% opacity
            //window.navigationBarColor = Color(0x80000000).toArgb() // 50% opacity
            //window.navigationBarColor = Color(0xCC000000).toArgb() // 80% opacity

            // #F5F5F5 transparent options
            *//* window.navigationBarColor = Color(0x40F5F5F5).toArgb() // 25% opacity
            window.navigationBarColor = Color(0x80F5F5F5).toArgb() // 50% opacity
            window.navigationBarColor = Color(0xCCF5F5F5).toArgb() // 80% opacity*//*

            //WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            //WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true


        }
    }*/

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}