package tech.appard.hvala.shared.feature.listings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingCard
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.core.ui.components.refresh.HvalaPullToRefreshBox
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.ui.utils.rememberNavigationBarBottomPadding
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingsViewModel
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingsUiState
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingsFilterSheet
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingsHeader

@Composable
fun ListingsScreen(
    viewModel: ListingsViewModel,
    modifier: Modifier = Modifier,
    showGuestLoginButton: Boolean = false,
    onLoginClick: () -> Unit = {},
    onListingClick: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    ListingsContent(
        modifier = modifier,
        state = state,
        showGuestLoginButton = showGuestLoginButton,
        onLoginClick = onLoginClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onCategorySelected = viewModel::onCategorySelected,
        onFilterClick = viewModel::onFilterClick,
        onListingFavoriteToggle = viewModel::onListingFavoriteToggle,
        onListingClick = onListingClick,
        onFilterDismiss = viewModel::onFilterDismiss,
        onDraftFiltersChange = viewModel::onDraftFiltersChange,
        onFilterReset = viewModel::onFilterReset,
        onFilterApply = viewModel::onFilterApply,
        onRefresh = viewModel::refresh,
    )
}

@Composable
private fun ListingsContent(
    state: ListingsUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onFilterClick: () -> Unit = {},
    onListingFavoriteToggle: (String) -> Unit,
    onListingClick: (String) -> Unit = {},
    onFilterDismiss: () -> Unit,
    onDraftFiltersChange: (UIListingsFilters) -> Unit,
    onFilterReset: () -> Unit,
    onFilterApply: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showGuestLoginButton: Boolean = false,
    onLoginClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current
    val navigationBarPadding = rememberNavigationBarBottomPadding()
    val gridState = rememberLazyGridState()
    val density = LocalDensity.current
    val collapseDistancePx = with(density) { dimensions.listingsHeaderCollapseDistance.toPx() }

    val collapseFraction by remember {
        derivedStateOf {
            if (gridState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (gridState.firstVisibleItemScrollOffset / collapseDistancePx).coerceIn(0f, 1f)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ListingsHeader(
                searchQuery = state.searchQuery,
                categories = state.categories,
                selectedCategoryId = state.selectedCategoryId,
                onSearchQueryChange = onSearchQueryChange,
                onCategorySelected = onCategorySelected,
                onFilterClick = onFilterClick,
                collapseFraction = collapseFraction,
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = SecondaryMain)
                }
            } else {
                HvalaPullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = dimensions.horizontalMedium,
                        end = dimensions.horizontalMedium,
                        top = dimensions.verticalMedium,
                        bottom = dimensions.verticalMedium + navigationBarPadding,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.listingGridSpacing),
                    verticalArrangement = Arrangement.spacedBy(dimensions.listingGridSpacing),
                ) {
                    items(
                        items = state.listings,
                        key = { it.id },
                    ) { listing ->
                        ListingCard(
                            listing = listing,
                            onFavoriteClick = { onListingFavoriteToggle(listing.id) },
                            onClick = { onListingClick(listing.id) },
                        )
                    }
                }
                }
            }
        }

        ListingsFilterSheet(
            visible = state.isFilterSheetVisible,
            draftFilters = state.draftFilters,
            categories = state.categories,
            countries = state.countries,
            regions = state.availableRegions,
            onDismiss = onFilterDismiss,
            onDraftChange = onDraftFiltersChange,
            onReset = onFilterReset,
            onApply = onFilterApply,
        )

        if (showGuestLoginButton) {
            IconButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = dimensions.horizontalMedium,
                        bottom = dimensions.horizontalMedium + navigationBarPadding,
                    )
                    .size(dimensions.iconButtonDefaultSize)
                    .shadow(
                        elevation = dimensions.bottomNavFabElevation,
                        shape = CircleShape,
                    )
                    .background(SecondaryMain, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = appStrings().listings.login,
                    tint = White,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        }
    }
}

@Composable
@Preview
private fun ListingsScreenPreview() {
    HvalaTheme {
        ListingsContent(
            state = ListingsUiState(
                categories = listOf(
                    UIListingCategory("clothes", "Одежда"),
                    UIListingCategory("auto", "Авто"),
                    UIListingCategory("realty", "Недвижимость"),
                    UIListingCategory("electronics", "Электроника"),
                    UIListingCategory("furniture", "Мебель"),
                ),
                listings = List(4) { index ->
                    UIListing(
                        id = "listing-$index",
                        title = "Худи Number Nine",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Химки, МО",
                    )
                },
            ),
            onSearchQueryChange = {},
            onCategorySelected = {},
            onFilterClick = {},
            onListingFavoriteToggle = {},
            onListingClick = {},
            onFilterDismiss = {},
            onDraftFiltersChange = {},
            onFilterReset = {},
            onFilterApply = {},
            onRefresh = {},
        )
    }
}
