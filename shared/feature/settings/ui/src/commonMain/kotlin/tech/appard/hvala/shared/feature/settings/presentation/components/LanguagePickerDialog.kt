package tech.appard.hvala.shared.feature.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.i18n.AppLanguage

@Composable
fun LanguagePickerDialog(
    title: String,
    languages: List<AppLanguage>,
    selectedLanguage: AppLanguage,
    cancelText: String,
    confirmText: String,
    onLanguageSelected: (AppLanguage) -> Unit,
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
                languages.forEach { language ->
                    LanguagePickerRow(
                        language = language,
                        isSelected = language == selectedLanguage,
                        onClick = { onLanguageSelected(language) },
                    )
                }
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
private fun LanguagePickerRow(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = dimensions.verticalXSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SecondaryMain,
                unselectedColor = GrayText,
            ),
        )
        Text(
            text = language.nativeName,
            style = BodyMedium.copy(color = InputText),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = language.flagEmoji,
            fontSize = 22.sp,
        )
    }
}
