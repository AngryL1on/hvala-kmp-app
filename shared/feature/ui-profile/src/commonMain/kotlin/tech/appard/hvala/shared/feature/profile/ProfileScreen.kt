package tech.appard.hvala.shared.feature.profile

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
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.ui.components.listings.ListingCard
import tech.appard.hvala.shared.feature.profile.components.ProfileHeaderCard
import tech.appard.hvala.shared.feature.profile.components.ProfileSegmentedTabs

@Composable
fun ProfileScreen(
    stateHolder: ProfileStateHolder,
    modifier: Modifier = Modifier,
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    ProfileContent(
        modifier = modifier,
        state = state,
        onTabSelected = stateHolder::onTabSelected,
        onListingFavoriteToggle = stateHolder::onListingFavoriteToggle,
    )
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onTabSelected: (ProfileListingsTab) -> Unit,
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
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensions.horizontalMedium),
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
                ) {
                    ProfileHeaderCard(
                        modifier = Modifier.padding(top = dimensions.verticalMedium),
                        name = formatProfileName(state.profile?.fullName),
                        activeListingsCount = state.activeListingsCount,
                        rating = state.rating,
                        memberSince = state.memberSince,
                    )

                    ProfileSegmentedTabs(
                        selectedTab = state.selectedTab,
                        onTabSelected = onTabSelected,
                    )

                    if (state.listings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (state.selectedTab == ProfileListingsTab.Archive) {
                                    "Архив пуст"
                                } else {
                                    "Нет объявлений"
                                },
                                style = BodyMedium.copy(color = GrayText),
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
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
                                    dimmed = state.selectedTab == ProfileListingsTab.Archive,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatProfileName(fullName: String?): String {
    if (fullName.isNullOrBlank()) return "Пользователь"
    val parts = fullName.trim().split("\\s+".toRegex())
    return when {
        parts.size >= 2 -> "${parts[0]} ${parts[1].firstOrNull()?.uppercaseChar() ?: ""}."
        else -> parts.first()
    }
}

@Composable
@Preview
private fun ProfileScreenPreview() {
    HvalaTheme {
        ProfileContent(
            state = ProfileUiState(
                profile = null,
                isLoading = false,
                activeListingsCount = 22,
                rating = 4.0f,
                memberSince = "На Hvala с июня 2024",
                activeListings = List(4) { index ->
                    Listing(
                        id = "listing-$index",
                        title = "Худи Number Nine",
                        priceUsd = 150,
                        priceRub = 12_570,
                        location = "Химки, МО",
                    )
                },
            ),
            onTabSelected = {},
            onListingFavoriteToggle = {},
        )
    }
}
