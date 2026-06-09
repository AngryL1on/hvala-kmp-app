package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
fun ReplyReviewDialog(
    title: String,
    replyTextLabel: String,
    replyText: String,
    onReplyTextChange: (String) -> Unit,
    replyTextPlaceholder: String,
    cancelText: String,
    submitText: String,
    errorText: String?,
    isSubmitting: Boolean,
    onSubmit: () -> Unit,
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
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
            ) {
                PrimaryTextField(
                    title = replyTextLabel,
                    value = replyText,
                    onTextChange = onReplyTextChange,
                    placeholder = replyTextPlaceholder,
                    minLines = 3,
                    maxLines = 6,
                )

                if (errorText != null) {
                    Text(
                        text = errorText,
                        style = BodyMedium.copy(color = Error),
                    )
                }
            }
        },
        confirmButton = {
            PrimaryButton(
                text = submitText,
                onClick = onSubmit,
                enabled = !isSubmitting,
                loading = isSubmitting,
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(
                    text = cancelText,
                    style = LinkMedium.copy(color = GrayText),
                )
            }
        },
    )
}
