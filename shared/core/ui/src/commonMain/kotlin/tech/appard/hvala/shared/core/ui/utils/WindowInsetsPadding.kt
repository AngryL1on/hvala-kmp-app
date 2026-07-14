package tech.appard.hvala.shared.core.ui.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
@Composable
fun rememberNavigationBarBottomPadding(min: Dp = 64.dp): Dp {
    val navigationBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val systemBottom = WindowInsets.systemBars
        .asPaddingValues()
        .calculateBottomPadding()
    return listOf(navigationBottom, systemBottom, min).max()
}
