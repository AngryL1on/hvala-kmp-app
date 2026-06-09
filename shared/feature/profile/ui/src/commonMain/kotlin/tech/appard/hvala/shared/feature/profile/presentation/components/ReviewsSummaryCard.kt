package tech.appard.hvala.shared.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleLarge
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReviewSummary

@Composable
fun ReviewsSummaryCard(
    summary: UIReviewSummary,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().reviews
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
            .border(1.dp, CardBorder, shape)
            .padding(dimensions.horizontalMedium),
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalMedium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(96.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
        ) {
            Text(
                text = formatRatingValue(summary.averageRating),
                style = TitleLarge.copy(color = InputText),
            )
            StarRatingRow(
                rating = summary.averageRating,
                starSize = dimensions.iconDefaultSize - 2.dp,
            )
            Text(
                text = strings.reviewsCount(summary.totalCount),
                style = FieldCaption.copy(color = GrayText),
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
        ) {
            (5 downTo 1).forEach { stars ->
                RatingDistributionRow(
                    stars = stars,
                    count = summary.ratingDistribution[stars].orZero(),
                    total = summary.totalCount,
                    label = strings.distributionLabel(stars),
                )
            }
        }
    }
}

@Composable
private fun RatingDistributionRow(
    stars: Int,
    count: Int,
    total: Int,
    label: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val fraction = if (total > 0) count.toFloat() / total else 0f

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
    ) {
        Text(
            text = label,
            style = FieldCaption.copy(color = GrayText),
            modifier = Modifier.width(28.dp),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(CardBorder),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (fraction > 0f) PrimaryMain else CardBorder),
            )
        }
        Text(
            text = count.toString(),
            style = FieldCaption.copy(color = GrayText),
            modifier = Modifier.width(20.dp),
            textAlign = TextAlign.End,
        )
    }
}

private fun Int?.orZero(): Int = this ?: 0
