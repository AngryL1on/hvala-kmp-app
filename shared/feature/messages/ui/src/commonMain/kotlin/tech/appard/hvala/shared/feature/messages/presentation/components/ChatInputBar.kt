package tech.appard.hvala.shared.feature.messages.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAttachClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().messages

    PrimaryTextField(
        modifier = modifier,
        value = value,
        onTextChange = onValueChange,
        placeholder = strings.messagePlaceholder,
        fieldMinHeight = dimensions.fieldsDefaultHeight,
        contentPadding = PaddingValues(
            horizontal = dimensions.horizontalXSmall,
            vertical = dimensions.verticalXSmall,
        ),
        leadingContent = {
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
            ) {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = strings.attachment,
                    tint = PrimaryMain,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
        trailingContent = {
            IconButton(
                onClick = onSendClick,
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = strings.send,
                    tint = PrimaryMain,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
    )
}
