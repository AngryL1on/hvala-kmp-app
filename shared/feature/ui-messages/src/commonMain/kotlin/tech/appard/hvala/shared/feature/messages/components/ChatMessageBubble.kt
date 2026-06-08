package tech.appard.hvala.shared.feature.messages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.contracts.model.ChatMessage
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.FieldInput
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    if (message.isDateDivider) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensions.verticalSmall),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = message.text,
                style = BodyMedium.copy(color = GrayText),
            )
        }
        return
    }

    val bubbleShape = RoundedCornerShape(dimensions.messageBubbleRadius)
    val horizontalAlignment = if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isOutgoing) White else SecondaryMain
    val textColor = if (message.isOutgoing) InputText else White
    val elevation = if (message.isOutgoing) 2.dp else 0.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensions.verticalXXSmall),
        contentAlignment = horizontalAlignment,
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = dimensions.horizontalMedium)
                .clip(bubbleShape),
            shape = bubbleShape,
            color = bubbleColor,
            shadowElevation = elevation,
        ) {
            Text(
                text = message.text,
                style = FieldInput.copy(color = textColor),
                modifier = Modifier.padding(
                    horizontal = dimensions.horizontalMedium,
                    vertical = dimensions.verticalSmall,
                ),
            )
        }
    }
}
