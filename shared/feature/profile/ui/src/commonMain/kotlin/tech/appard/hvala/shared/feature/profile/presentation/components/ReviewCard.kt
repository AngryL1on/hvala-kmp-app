package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReview

@Composable
fun ReviewCard(
    review: UIReview,
    onReplyClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().reviews
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
            .border(1.dp, CardBorder, shape)
            .padding(dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReviewAuthorAvatar(
                initials = review.authorInitials,
                colorArgb = review.avatarColorArgb,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
            ) {
                Text(
                    text = review.authorName,
                    style = TitleMedium.copy(color = InputText),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = review.postedAt,
                    style = FieldCaption.copy(color = GrayText),
                )
            }
            StarRatingRow(
                rating = review.rating.toFloat(),
                starSize = dimensions.iconDefaultSize - 6.dp,
            )
        }

        review.listingTitle?.takeIf { it.isNotBlank() }?.let { listingTitle ->
            Text(
                text = strings.basedOnListing(listingTitle),
                style = FieldCaption.copy(color = GrayText),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = review.text,
            style = BodyMedium.copy(color = InputText),
        )

        ReviewPhotoGallery(photoUris = review.photoUris)

        review.sellerReply?.let { reply ->
            ReviewSellerReplySection(
                sellerReplyLabel = strings.sellerReplyLabel,
                authorName = reply.authorName,
                postedAt = reply.postedAt,
                text = reply.text,
            )
        }

        if (review.canReply && onReplyClick != null) {
            TextButton(onClick = onReplyClick) {
                Text(
                    text = strings.replyToReview,
                    style = LinkMedium.copy(color = SecondaryMain),
                )
            }
        }
    }
}

@Composable
private fun ReviewAuthorAvatar(
    initials: String,
    colorArgb: Long,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .size(dimensions.settingsEditBadgeSize + 8.dp)
            .clip(CircleShape)
            .background(Color(colorArgb)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = FieldCaption.copy(color = White),
        )
    }
}
