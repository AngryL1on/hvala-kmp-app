package tech.appard.hvala.shared.feature.messages.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconPlaceholder
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconVariant
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ChatListingCard(
    title: String,
    priceUsd: Int,
    priceRub: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
            .border(1.dp, CardBorder, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(dimensions.horizontalMedium),
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HvalaAppIconPlaceholder(
            modifier = Modifier.size(dimensions.chatListingImageSize),
            variant = HvalaAppIconVariant.Compact,
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
        ) {
            Text(
                text = title,
                style = TitleMedium.copy(color = InputText),
            )
            Text(
                text = "$priceUsd\$ ≈ ${formatPrice(priceRub)} ₽",
                style = BodyMedium.copy(color = GrayText),
            )
        }
    }
}

private fun formatPrice(priceRub: Int): String =
    priceRub.toString().reversed().chunked(3).joinToString(" ").reversed()
