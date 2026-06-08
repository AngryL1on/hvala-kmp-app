package tech.appard.hvala.shared.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.ui.components.listings.ListingCard
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain

@Composable
fun FavoritesScreen(
    stateHolder: FavoritesStateHolder,
    modifier: Modifier = Modifier,
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    FavoritesContent(
        modifier = modifier,
        state = state,
        onListingFavoriteToggle = stateHolder::onListingFavoriteToggle,
    )
}

@Composable
private fun FavoritesContent(
    state: FavoritesUiState,
    onListingFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

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
            state.listings.isEmpty() -> {
                Text(
                    text = "В избранном пока ничего нет",
                    style = BodyMedium.copy(color = GrayText),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            else -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
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
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun FavoritesScreenPreview() {
    HvalaTheme {
        FavoritesContent(
            state = FavoritesUiState(
                listings = List(4) { index ->
                    Listing(
                        id = "favorite-$index",
                        title = "Худи Number Nine",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Химки, МО",
                        isFavorite = true,
                    )
                },
            ),
            onListingFavoriteToggle = {},
        )
    }
}
