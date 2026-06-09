package tech.appard.hvala.shared.feature.listings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconPlaceholder
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconVariant
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.OverlayDark
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing

@Composable
fun ListingCard(
    listing: UIListing,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    dimmed: Boolean = false,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
            .background(White)
            .border(
                width = 1.dp,
                color = CardBorder,
                shape = RoundedCornerShape(dimensions.defaultCornerRadius),
            )
            .then(if (dimmed) Modifier.alpha(0.4f) else Modifier)
            .then(
                if (!dimmed) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            ),
    ) {
        ListingCardImage(
            listing = listing,
            onFavoriteClick = onFavoriteClick,
            interactive = !dimmed,
        )

        Column(
            modifier = Modifier.padding(dimensions.horizontalXSmall),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
        ) {
            Text(
                text = listing.title,
                style = BodyMedium.copy(color = InputText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXXSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${listing.priceUsd}$",
                    style = TitleMedium.copy(color = InputText),
                )
                Text(
                    text = "= ${formatPriceRub(listing.priceRub)} ₽",
                    style = FieldCaption.copy(color = GrayText),
                )
            }
            Text(
                text = listing.location,
                style = FieldCaption.copy(color = GrayText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ListingCardImage(
    listing: UIListing,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactive: Boolean = true,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().listings
    val favoriteBackground = if (listing.isFavorite) {
        SecondaryMain
    } else {
        GrayText.copy(alpha = 0.6f)
    }
    val favoriteIcon = if (listing.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder
    val favoriteIconTint = if (listing.isFavorite) PrimaryMain else GrayText

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        if (listing.imageUrl.isNullOrBlank()) {
            HvalaAppIconPlaceholder(
                modifier = Modifier.fillMaxSize(),
                variant = HvalaAppIconVariant.Full,
                iconScale = 0.45f,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dimensions.horizontalXSmall)
                .size(dimensions.iconButtonDefaultSize - 4.dp)
                .clip(CircleShape)
                .background(favoriteBackground)
                .then(
                    if (interactive) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onFavoriteClick,
                        )
                    } else {
                        Modifier
                    },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = favoriteIcon,
                contentDescription = if (listing.isFavorite) strings.removeFromFavorites else strings.addToFavorites,
                tint = favoriteIconTint,
                modifier = Modifier.size(dimensions.iconDefaultSize - 4.dp),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensions.horizontalXSmall)
                .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
                .background(OverlayDark)
                .padding(
                    horizontal = dimensions.horizontalXSmall,
                    vertical = dimensions.verticalXXSmall,
                ),
        ) {
            Text(
                text = "${listing.currentImage}/${listing.totalImages}",
                style = FieldCaption.copy(color = White),
            )
        }
    }
}

private fun formatPriceRub(priceRub: Int): String =
    priceRub.toString().reversed().chunked(3).joinToString(" ").reversed()
