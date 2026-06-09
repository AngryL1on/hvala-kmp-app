package tech.appard.hvala.shared.core.ui.components.controls

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryDisabled
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SwitchTrackOff
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun HvalaSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val dimensions = LocalDimensions.current
    val interactionSource = remember { MutableInteractionSource() }

    val trackColor = when {
        !enabled && checked -> PrimaryDisabled
        checked -> PrimaryMain
        else -> SwitchTrackOff
    }

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) {
            dimensions.switchTrackWidth - dimensions.switchThumbSize - dimensions.switchThumbPadding
        } else {
            dimensions.switchThumbPadding
        },
        animationSpec = tween(durationMillis = 200),
        label = "hvalaSwitchThumb",
    )

    Box(
        modifier = modifier
            .size(
                width = dimensions.switchTrackWidth,
                height = dimensions.switchTrackHeight,
            )
            .semantics {
                role = Role.Switch
                toggleableState = if (checked) ToggleableState.On else ToggleableState.Off
            }
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) },
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .shadow(
                    elevation = dimensions.switchThumbElevation,
                    shape = CircleShape,
                    clip = false,
                )
                .size(dimensions.switchThumbSize)
                .clip(CircleShape)
                .background(White),
        )
    }
}
