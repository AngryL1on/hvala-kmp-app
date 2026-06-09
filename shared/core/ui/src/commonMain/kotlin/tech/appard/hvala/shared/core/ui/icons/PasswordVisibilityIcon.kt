package tech.appard.hvala.shared.core.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder

@Composable
internal fun PasswordVisibilityIcon(
    visible: Boolean,
    modifier: Modifier = Modifier,
    tint: Color = GrayPlaceholder,
) {
    val strings = appStrings().common
    Icon(
        imageVector = if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
        contentDescription = if (visible) strings.hidePassword else strings.showPassword,
        modifier = modifier,
        tint = tint,
    )
}
