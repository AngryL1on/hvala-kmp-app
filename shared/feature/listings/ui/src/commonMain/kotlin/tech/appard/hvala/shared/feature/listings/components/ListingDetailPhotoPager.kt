package tech.appard.hvala.shared.feature.listings.components

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.feature.listings.ui.model.UIListing
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconPlaceholder
import tech.appard.hvala.shared.core.ui.components.logo.HvalaAppIconVariant
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.OverlayDark
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun ListingDetailPhotoPager(
    listing: UIListing,
    currentPhotoIndex: Int,
    onPhotoSelected: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)
    val photoCount = listing.totalImages.coerceAtLeast(1)
    val pagerState = rememberPagerState(
        initialPage = currentPhotoIndex.coerceIn(0, photoCount - 1),
        pageCount = { photoCount },
    )

    LaunchedEffect(currentPhotoIndex) {
        if (pagerState.currentPage != currentPhotoIndex) {
            pagerState.animateScrollToPage(currentPhotoIndex.coerceIn(0, photoCount - 1))
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != currentPhotoIndex) {
            onPhotoSelected(pagerState.currentPage)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.1f)
            .clip(shape)
            .border(width = 1.dp, color = CardBorder, shape = shape),
    ) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) {
            HvalaAppIconPlaceholder(
                modifier = Modifier.fillMaxSize(),
                variant = HvalaAppIconVariant.Full,
                iconScale = 0.45f,
            )
        }

        val favoriteBackground = if (listing.isFavorite) SecondaryMain else GrayText.copy(alpha = 0.6f)
        val favoriteIcon = if (listing.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder
        val favoriteIconTint = if (listing.isFavorite) PrimaryMain else GrayText

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dimensions.horizontalXSmall)
                .size(dimensions.iconButtonDefaultSize)
                .clip(CircleShape)
                .background(favoriteBackground)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onFavoriteClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = favoriteIcon,
                contentDescription = if (listing.isFavorite) "Remove from favorites" else "Add to favorites",
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
                text = "${pagerState.currentPage + 1}/$photoCount",
                style = FieldCaption.copy(color = White),
            )
        }
    }
}

@Composable
fun ListingDetailInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    rows: List<Pair<String, String>>,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
            .border(width = 1.dp, color = CardBorder, shape = shape)
            .padding(dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
    ) {
        Text(
            text = title,
            style = FieldCaption.copy(color = GrayText),
        )

        rows.filter { it.second.isNotBlank() }.forEach { (label, value) ->
            ListingDetailInfoRow(label = label, value = value)
        }
    }
}

@Composable
private fun ListingDetailInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = label,
            style = FieldCaption.copy(color = GrayText),
            modifier = Modifier.weight(0.45f),
        )
        Text(
            text = value,
            style = FieldCaption.copy(color = InputText),
            modifier = Modifier.weight(0.55f),
        )
    }
}
