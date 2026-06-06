package tech.appard.hvala.shared.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimensions(
    val horizontalMedium: Dp = 16.dp,
)

val LocalDimensions = compositionLocalOf { Dimensions() }
