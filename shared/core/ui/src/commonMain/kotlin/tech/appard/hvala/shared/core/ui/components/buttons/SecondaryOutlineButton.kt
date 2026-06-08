package tech.appard.hvala.shared.core.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.theme.ButtonMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun SecondaryOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: TextStyle = ButtonMedium,
) {
    val dimensions = LocalDimensions.current

    OutlinedButton(
        modifier = modifier.height(dimensions.verticalHuge),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(dimensions.defaultCornerRadius),
        border = BorderStroke(1.dp, PrimaryMain),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = White,
            contentColor = GrayText,
            disabledContainerColor = White,
            disabledContentColor = GrayText.copy(alpha = 0.5f),
        ),
    ) {
        Text(
            text = text,
            style = textStyle,
        )
    }
}
