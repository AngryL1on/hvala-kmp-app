package tech.appard.hvala.shared.core.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder

@Composable
internal fun PasswordVisibilityIcon(
    visible: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = GrayPlaceholder,
) {
    Icon(
        imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        contentDescription = if (visible) "Hide password" else "Show password",
        modifier = modifier,
        tint = tint,
    )
}
