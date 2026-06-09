package tech.appard.hvala.shared.feature.favorites.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.feature.listings.presentation.components.FilterSettingsSheet
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingCard
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingSortOrder
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.favorites.presentation.viewmodels.FavoritesStateHolder
import tech.appard.hvala.shared.feature.favorites.presentation.viewmodels.FavoritesUiState
import tech.appard.hvala.shared.feature.favorites.presentation.components.FavoritesToolbar

@Composable
fun FavoritesScreen(
    stateHolder: FavoritesStateHolder,
    modifier: Modifier = Modifier,
    onListingClick: (String) -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    FavoritesContent(
        modifier = modifier,
        state = state,
        onSortOrderChange = stateHolder::onSortOrderChange,
        onFilterClick = stateHolder::onFilterClick,
        onListingFavoriteToggle = stateHolder::onListingFavoriteToggle,
        onListingClick = onListingClick,
        onFilterDismiss = stateHolder::onFilterDismiss,
        onDraftFiltersChange = stateHolder::onDraftFiltersChange,
        onDraftSortOrderChange = stateHolder::onDraftSortOrderChange,
        onFilterReset = stateHolder::onFilterReset,
        onFilterApply = stateHolder::onFilterApply,
    )
}

@Composable
private fun FavoritesContent(
    state: FavoritesUiState,
    onSortOrderChange: (UIListingSortOrder) -> Unit,
    onFilterClick: () -> Unit,
    onListingFavoriteToggle: (String) -> Unit,
    onListingClick: (String) -> Unit,
    onFilterDismiss: () -> Unit,
    onDraftFiltersChange: (UIListingsFilters) -> Unit,
    onDraftSortOrderChange: (UIListingSortOrder) -> Unit,
    onFilterReset: () -> Unit,
    onFilterApply: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SecondaryMain,
                )
            }
            !state.hasAnyFavorites -> {
                Text(
                    text = strings.favorites.empty,
                    style = BodyMedium.copy(color = GrayText),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    FavoritesToolbar(
                        sortOrder = state.sortOrder,
                        onSortOrderChange = onSortOrderChange,
                        onFilterClick = onFilterClick,
                    )

                    if (state.listings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = strings.favorites.noFilterResults,
                                style = BodyMedium.copy(color = GrayText),
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(horizontal = dimensions.horizontalMedium),
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                top = dimensions.verticalMedium,
                                bottom = dimensions.verticalLarge,
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
        }

        FilterSettingsSheet(
            visible = state.isFilterSheetVisible,
            draftFilters = state.draftFilters,
            categories = state.categories,
            countries = state.countries,
            regions = state.availableRegions,
            onDismiss = onFilterDismiss,
            onDraftChange = onDraftFiltersChange,
            onReset = onFilterReset,
            onApply = onFilterApply,
            title = strings.listings.filterTitle,
            sortOrder = state.draftSortOrder,
            onSortOrderChange = onDraftSortOrderChange,
        )
    }
}

@Composable
@Preview
private fun FavoritesScreenPreview() {
    HvalaTheme {
        FavoritesContent(
            state = FavoritesUiState(
                allListings = List(4) { index ->
                    UIListing(
                        id = "favorite-$index",
                        title = "Худи Number Nine",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Химки, МО",
                        isFavorite = true,
                    )
                },
                listings = List(4) { index ->
                    UIListing(
                        id = "favorite-$index",
                        title = "Худи Number Nine",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Химки, МО",
                        isFavorite = true,
                    )
                },
            ),
            onSortOrderChange = {},
            onFilterClick = {},
            onListingFavoriteToggle = {},
            onListingClick = {},
            onFilterDismiss = {},
            onDraftFiltersChange = {},
            onDraftSortOrderChange = {},
            onFilterReset = {},
            onFilterApply = {},
        )
    }
}
