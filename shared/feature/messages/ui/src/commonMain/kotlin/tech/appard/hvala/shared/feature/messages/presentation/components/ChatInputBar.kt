package tech.appard.hvala.shared.feature.messages.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAttachClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current

    PrimaryTextField(
        modifier = modifier,
        value = value,
        onTextChange = onValueChange,
        placeholder = "Сообщение",
        fieldMinHeight = dimensions.chatInputHeight,
        contentPadding = PaddingValues(
            horizontal = dimensions.horizontalXSmall,
            vertical = dimensions.verticalXSmall,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        isMaxQuantityOfCharVisible = false,
        leadingContent = {
            IconButton(
                onClick = onAttachClick,
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
            ) {
                Icon(
                    imageVector = Icons.Outlined.AttachFile,
                    contentDescription = "Вложение",
                    tint = GrayPlaceholder,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
        trailingContent = {
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(dimensions.iconButtonDefaultSize)
                    .clip(CircleShape)
                    .background(SecondaryMain),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Отправить",
                    tint = White,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
    )
}
