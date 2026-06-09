package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.components.image.LocalUriImage
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconPlaceholder
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconVariant
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.InputBorder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ProfileHeaderCard(
    name: String,
    activeListingsCount: Int,
    rating: Float,
    memberSince: String,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
    onReviewsClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().profile

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensions.defaultCornerRadius))
            .background(White)
            .border(
                width = 1.dp,
                color = CardBorder,
                shape = RoundedCornerShape(dimensions.defaultCornerRadius),
            )
            .padding(dimensions.horizontalMedium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
        ) {
            Text(
                text = name,
                style = TitleMedium.copy(color = InputText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = strings.activeListingsCount(activeListingsCount),
                style = BodyMedium.copy(color = GrayText),
            )
            ProfileRatingRow(
                rating = rating,
                reviewsLabel = strings.reviews,
                onReviewsClick = onReviewsClick,
            )
            Text(
                text = memberSince,
                style = FieldCaption.copy(color = GrayText),
            )
        }

        ProfileAvatar(avatarUrl = avatarUrl)
    }
}

@Composable
private fun ProfileRatingRow(
    rating: Float,
    reviewsLabel: String,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXSmall),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXXSmall),
        ) {
            Text(
                text = formatRatingValue(rating),
                style = BodyMedium.copy(color = InputText),
            )
            StarRatingRow(rating = rating)
        }

        ProfileReviewsButton(
            label = reviewsLabel,
            onClick = onReviewsClick,
        )
    }
}

@Composable
private fun ProfileReviewsButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Row(
        modifier = modifier
            .clip(shape)
            .background(PrimaryMain.copy(alpha = 0.18f))
            .border(width = 1.dp, color = InputBorder, shape = shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(
                horizontal = dimensions.horizontalSmall,
                vertical = dimensions.verticalXSmall,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXXSmall),
    ) {
        Text(
            text = label,
            style = BodyMedium.copy(color = InputText),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = PrimaryMain,
            modifier = Modifier.size(dimensions.iconDefaultSize - 4.dp),
        )
    }
}

@Composable
private fun ProfileAvatar(
    avatarUrl: String?,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .size(dimensions.avatarSize)
            .clip(CircleShape)
            .border(
                width = dimensions.avatarBorderWidth,
                color = PrimaryMain,
                shape = CircleShape,
            )
            .background(White),
        contentAlignment = Alignment.Center,
    ) {
        if (avatarUrl.isNullOrBlank()) {
            HvalaAppIconPlaceholder(
                modifier = Modifier.fillMaxSize(),
                variant = HvalaAppIconVariant.Full,
                showBackground = false,
                iconScale = 0.5f,
            )
        } else {
            LocalUriImage(
                uri = avatarUrl,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
