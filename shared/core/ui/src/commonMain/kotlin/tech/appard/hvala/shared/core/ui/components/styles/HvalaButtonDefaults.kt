package tech.appard.hvala.shared.core.ui.components.styles

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.ui.theme.PrimaryDisabled
import tech.appard.hvala.shared.core.ui.theme.White

object HvalaButtonDefaults {
    @Composable
    fun primaryColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        contentColor: Color = White,
        disabledContainerColor: Color = PrimaryDisabled,
        disabledContentColor: Color = White.copy(alpha = 0.8f),
    ): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )
}