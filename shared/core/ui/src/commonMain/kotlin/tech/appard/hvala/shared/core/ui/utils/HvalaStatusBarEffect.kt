package tech.appard.hvala.shared.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
expect fun HvalaStatusBarEffect(
    statusBarColor: Color,
    navigationBarColor: Color = White,
    darkStatusBarIcons: Boolean = true,
    darkNavigationBarIcons: Boolean = true,
)
