package tech.appard.hvala.shared.feature.profile.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingCard
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.profile.presentation.model.UISellerProfile
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.SellerProfileStateHolder
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.SellerProfileUiState
import tech.appard.hvala.shared.feature.profile.presentation.components.ProfileHeaderCard

@Composable
fun SellerProfileScreen(
    sellerId: String,
    stateHolder: SellerProfileStateHolder,
    modifier: Modifier = Modifier,
    onListingClick: (String) -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(sellerId) {
        stateHolder.load(sellerId)
    }

    SellerProfileContent(
        modifier = modifier,
        state = state,
        onListingFavoriteToggle = stateHolder::onListingFavoriteToggle,
        onListingClick = onListingClick,
    )
}

@Composable
private fun SellerProfileContent(
    state: SellerProfileUiState,
    onListingFavoriteToggle: (String) -> Unit,
    onListingClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().profile

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
            state.seller == null -> {
                Text(
                    text = state.error ?: strings.sellerNotFound,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(dimensions.horizontalMedium),
                    style = BodyMedium.copy(color = GrayText),
                    textAlign = TextAlign.Center,
                )
            }
            else -> {
                val seller = state.seller

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensions.horizontalMedium),
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
                ) {
                    ProfileHeaderCard(
                        modifier = Modifier.padding(top = dimensions.verticalMedium),
                        name = seller.name,
                        activeListingsCount = seller.activeListingsCount,
                        rating = seller.rating,
                        memberSince = seller.memberSince,
                    )

                    if (state.listings.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = strings.noActiveListings,
                                style = BodyMedium.copy(color = GrayText),
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(bottom = dimensions.verticalLarge),
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
    }
}

@Composable
@Preview
private fun SellerProfileScreenPreview() {
    HvalaTheme {
        SellerProfileContent(
            state = SellerProfileUiState(
                seller = UISellerProfile(
                    id = "niko",
                    name = "Niko B.",
                    activeListingsCount = 8,
                    rating = 4.8f,
                    memberSince = "On Hvala since March 2024",
                ),
                listings = List(4) { index ->
                    UIListing(
                        id = "listing-$index",
                        title = "Number Nine Hoodie",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Khimki, Moscow Region",
                        sellerId = "niko",
                        sellerName = "Niko B.",
                    )
                },
            ),
            onListingFavoriteToggle = {},
            onListingClick = {},
        )
    }
}
