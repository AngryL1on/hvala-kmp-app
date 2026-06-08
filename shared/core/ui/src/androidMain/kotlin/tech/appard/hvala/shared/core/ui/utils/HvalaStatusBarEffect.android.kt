package tech.appard.hvala.shared.core.ui.utils

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
actual fun HvalaStatusBarEffect(
    statusBarColor: Color,
    navigationBarColor: Color,
    darkStatusBarIcons: Boolean,
    darkNavigationBarIcons: Boolean,
) {
    val activity = LocalActivity.current as? ComponentActivity ?: return

    SideEffect {
        activity.enableEdgeToEdge(
            statusBarStyle = systemBarStyle(statusBarColor, darkStatusBarIcons),
            navigationBarStyle = systemBarStyle(navigationBarColor, darkNavigationBarIcons),
        )
    }
}

private fun systemBarStyle(color: Color, darkIcons: Boolean): SystemBarStyle =
    if (darkIcons) {
        SystemBarStyle.light(
            scrim = color.toArgb(),
            darkScrim = color.toArgb(),
        )
    } else {
        SystemBarStyle.dark(scrim = color.toArgb())
    }
