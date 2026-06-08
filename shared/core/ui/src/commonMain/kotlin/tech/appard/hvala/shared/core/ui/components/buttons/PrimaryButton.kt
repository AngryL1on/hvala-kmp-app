package tech.appard.hvala.shared.core.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.theme.ButtonMedium
import tech.appard.hvala.shared.core.ui.components.styles.HvalaButtonDefaults
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    leadingIcon: ImageVector? = null,
    textStyle: TextStyle = ButtonMedium,
) {
    val dimensions = LocalDimensions.current

    Button(
        modifier = modifier.height(dimensions.verticalHuge),
        onClick = onClick,
        enabled = enabled && !loading,
        shape = RoundedCornerShape(dimensions.defaultCornerRadius),
        colors = HvalaButtonDefaults.primaryColors(),
    ) {
        val contentColor = LocalContentColor.current

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .align(Alignment.CenterVertically),
                color = contentColor,
                strokeCap = StrokeCap.Round,
                strokeWidth = dimensions.circularStrokeWith,
            )
        } else {
            leadingIcon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(dimensions.horizontalXSmall))
            }
            Text(
                text = text,
                style = textStyle,
                color = contentColor,
            )
        }
    }
}

@Composable
@Preview
private fun PrimaryButtonPreview() {
    HvalaTheme {
        val compactButton = Modifier
            .width(140.dp)
            .height(40.dp)
        val wideButton = Modifier
            .width(280.dp)
            .height(56.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PrimaryButton(
                    modifier = compactButton,
                    text = "Button",
                    onClick = {},
                )
                PrimaryButton(
                    modifier = wideButton,
                    text = "Button",
                    onClick = {},
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PrimaryButton(
                    modifier = compactButton,
                    text = "Button",
                    loading = true,
                    onClick = {},
                )
                PrimaryButton(
                    modifier = wideButton,
                    text = "Button",
                    loading = true,
                    onClick = {},
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PrimaryButton(
                    modifier = compactButton,
                    text = "Button",
                    enabled = false,
                    onClick = {},
                )
                PrimaryButton(
                    modifier = wideButton,
                    text = "Button",
                    enabled = false,
                    onClick = {},
                )
            }
        }
    }
}
