package tech.appard.hvala.shared.feature.profile.presentation.screens

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingCard
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ProfileStateHolder
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ProfileUiState
import tech.appard.hvala.shared.feature.profile.presentation.components.ProfileHeaderCard
import tech.appard.hvala.shared.feature.profile.presentation.components.ProfileSegmentedTabs
import tech.appard.hvala.shared.feature.profile.presentation.model.ProfileListingsTab
import tech.appard.hvala.shared.feature.profile.presentation.model.pageIndex
import tech.appard.hvala.shared.feature.profile.presentation.model.profileListingsTab

@Composable
fun ProfileScreen(
    stateHolder: ProfileStateHolder,
    modifier: Modifier = Modifier,
    onListingClick: (String) -> Unit = {},
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
        onListingClick = onListingClick,
    )
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onTabSelected: (ProfileListingsTab) -> Unit,
    onListingFavoriteToggle: (String) -> Unit,
    onListingClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val pagerState = rememberPagerState(initialPage = state.selectedTab.pageIndex) {
        ProfileListingsTab.entries.size
    }

    LaunchedEffect(state.selectedTab) {
        val targetPage = state.selectedTab.pageIndex
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }
            .filter { !it.second }
            .distinctUntilChanged()
            .collect { (page, _) ->
                onTabSelected(profileListingsTab(page))
            }
    }

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

                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        state = pagerState,
                        beyondViewportPageCount = 1,
                    ) { page ->
                        val tab = profileListingsTab(page)
                        val listings = when (tab) {
                            ProfileListingsTab.Active -> state.activeListings
                            ProfileListingsTab.Archive -> state.archiveListings
                        }

                        ProfileListingsPage(
                            listings = listings,
                            isArchive = tab == ProfileListingsTab.Archive,
                            onListingFavoriteToggle = onListingFavoriteToggle,
                            onListingClick = onListingClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileListingsPage(
    listings: List<UIListing>,
    isArchive: Boolean,
    onListingFavoriteToggle: (String) -> Unit,
    onListingClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().profile

    if (listings.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (isArchive) strings.archiveEmpty else strings.listingsEmpty,
                style = BodyMedium.copy(color = GrayText),
            )
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = dimensions.verticalLarge),
            horizontalArrangement = Arrangement.spacedBy(dimensions.listingGridSpacing),
            verticalArrangement = Arrangement.spacedBy(dimensions.listingGridSpacing),
        ) {
            items(
                items = listings,
                key = { it.id },
            ) { listing ->
                ListingCard(
                    listing = listing,
                    onFavoriteClick = { onListingFavoriteToggle(listing.id) },
                    onClick = { onListingClick(listing.id) },
                    dimmed = isArchive,
                )
            }
        }
    }
}

@Composable
private fun formatProfileName(fullName: String?): String {
    if (fullName.isNullOrBlank()) return appStrings().common.defaultUserName
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
                    UIListing(
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
            onListingClick = {},
        )
    }
}
