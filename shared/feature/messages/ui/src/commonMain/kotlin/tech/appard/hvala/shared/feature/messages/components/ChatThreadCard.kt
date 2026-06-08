package tech.appard.hvala.shared.feature.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.feature.messages.ui.model.UIChatThread
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ChatThreadCard(
    thread: UIChatThread,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        shape = shape,
        color = White,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.horizontalMedium),
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ChatAvatar(
                colorArgb = thread.avatarColorArgb,
                size = dimensions.chatThreadAvatarSize,
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
            ) {
                Text(
                    text = thread.participantName,
                    style = TitleMedium.copy(color = InputText),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = thread.lastMessagePreview,
                    style = BodyMedium.copy(color = GrayText),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
