package tech.appard.hvala.shared.feature.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.components.controls.HvalaSwitch
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.feature.settings.domain.model.NotificationPreferences

@Composable
fun NotificationSettingsDialog(
    title: String,
    notificationsLabel: String,
    soundLabel: String,
    cancelText: String,
    confirmText: String,
    preferences: NotificationPreferences,
    onNotificationsChanged: (Boolean) -> Unit,
    onSoundChanged: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = FieldTitle.copy(color = InputText),
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
            ) {
                NotificationToggleRow(
                    label = notificationsLabel,
                    checked = preferences.notificationsEnabled,
                    onCheckedChange = onNotificationsChanged,
                )
                NotificationToggleRow(
                    label = soundLabel,
                    checked = preferences.soundEnabled,
                    onCheckedChange = onSoundChanged,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = LinkMedium.copy(color = SecondaryMain),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = cancelText,
                    style = LinkMedium.copy(color = GrayText),
                )
            }
        },
    )
}

@Composable
private fun NotificationToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) },
            )
            .padding(vertical = dimensions.verticalXSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
    ) {
        Text(
            text = label,
            style = BodyMedium.copy(color = InputText),
            modifier = Modifier.weight(1f),
        )
        HvalaSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
