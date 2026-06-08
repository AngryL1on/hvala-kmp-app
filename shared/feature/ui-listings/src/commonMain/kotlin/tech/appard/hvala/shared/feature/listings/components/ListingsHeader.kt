package tech.appard.hvala.shared.feature.listings.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.contracts.model.ListingCategory
import tech.appard.hvala.shared.core.ui.components.logo.HvalaLogo
import tech.appard.hvala.shared.core.ui.components.logo.HvalaLogoAspectRatio
import tech.appard.hvala.shared.core.ui.components.logo.HvalaLogoVariant
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain

@Composable
fun ListingsHeader(
    searchQuery: String,
    categories: List<ListingCategory>,
    selectedCategoryId: String?,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onFilterClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    collapseFraction: Float = 0f,
) {
    val dimensions = LocalDimensions.current
    val headerShape = RoundedCornerShape(
        bottomStart = dimensions.defaultCornerRadius,
        bottomEnd = dimensions.defaultCornerRadius,
    )
    val animatedCollapse by animateFloatAsState(
        targetValue = collapseFraction.coerceIn(0f, 1f),
        label = "listingsHeaderCollapse",
    )
    val logoScale = 1f - animatedCollapse * 0.9f
    val logoAspectRatio = HvalaLogoAspectRatio
    val fullLogoHeight = dimensions.listingsLogoSize / logoAspectRatio
    val logoAreaHeight = fullLogoHeight * logoScale

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(headerShape)
            .background(PrimaryMain),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    top = dimensions.verticalXXSmall,
                    bottom = dimensions.verticalMedium,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(logoAreaHeight.coerceAtLeast(0.dp))
                    .graphicsLayer { clip = true },
                contentAlignment = Alignment.Center,
            ) {
                if (logoScale > 0.05f) {
                    HvalaLogo(
                        modifier = Modifier.graphicsLayer {
                            alpha = logoScale
                            scaleX = logoScale
                            scaleY = logoScale
                        },
                        variant = HvalaLogoVariant.OnPrimaryBackground,
                        width = dimensions.listingsLogoSize * logoScale,
                    )
                }
            }

            ListingsSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onFilterClick = onFilterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensions.horizontalMedium)
                    .padding(top = dimensions.verticalSmall),
            )

            ListingsCategoryRow(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategorySelected = onCategorySelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensions.verticalSmall),
            )
        }
    }
}
