package tech.appard.hvala.shared.feature.profile

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.contracts.model.UserProfile
import tech.appard.hvala.shared.core.contracts.repository.ProfileRepository

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val activeListingsCount: Int = 0,
    val rating: Float = 0f,
    val memberSince: String = "",
    val selectedTab: ProfileListingsTab = ProfileListingsTab.Active,
    val activeListings: List<Listing> = emptyList(),
    val archiveListings: List<Listing> = emptyList(),
) {
    val listings: List<Listing>
        get() = when (selectedTab) {
            ProfileListingsTab.Active -> activeListings
            ProfileListingsTab.Archive -> archiveListings
        }
}

class ProfileStateHolder(
    private val profileRepository: ProfileRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    fun reset() {
        _state.value = ProfileUiState()
    }

    fun load() {
        if (_state.value.profile != null || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val profile = profileRepository.getCurrentProfile()
            _state.update {
                it.copy(
                    profile = profile,
                    isLoading = false,
                    activeListingsCount = 22,
                    rating = 4.0f,
                    memberSince = "На Hvala с июня 2024",
                    activeListings = mockActiveListings(),
                    archiveListings = mockArchiveListings(),
                )
            }
        }
    }

    fun onTabSelected(tab: ProfileListingsTab) {
        _state.update { current ->
            if (current.selectedTab == tab) return@update current
            current.copy(selectedTab = tab)
        }
    }

    fun onListingFavoriteToggle(listingId: String) {
        _state.update { current ->
            current.copy(
                activeListings = toggleFavorite(current.activeListings, listingId),
                archiveListings = toggleFavorite(current.archiveListings, listingId),
            )
        }
    }

    private fun toggleFavorite(
        listings: List<Listing>,
        listingId: String,
    ): List<Listing> = listings.map { listing ->
        if (listing.id == listingId) {
            listing.copy(isFavorite = !listing.isFavorite)
        } else {
            listing
        }
    }

    private fun mockActiveListings(): List<Listing> = List(8) { index ->
        Listing(
            id = "active-$index",
            title = "Худи Number Nine",
            priceUsd = 150,
            priceRub = 12_570,
            location = "Химки, МО",
            isFavorite = index == 1,
        )
    }

    private fun mockArchiveListings(): List<Listing> = listOf(
        Listing(
            id = "archive-0",
            title = "Кроссовки Nike Air Max",
            priceUsd = 90,
            priceRub = 7_540,
            location = "Москва",
            isFavorite = true,
        ),
        Listing(
            id = "archive-1",
            title = "iPhone 13 Pro 256GB",
            priceUsd = 620,
            priceRub = 51_900,
            location = "Санкт-Петербург",
        ),
        Listing(
            id = "archive-2",
            title = "Пальто Burberry",
            priceUsd = 280,
            priceRub = 23_450,
            location = "Химки, МО",
            isFavorite = true,
        ),
        Listing(
            id = "archive-3",
            title = "Велосипед Trek",
            priceUsd = 410,
            priceRub = 34_300,
            location = "Красногорск, МО",
        ),
        Listing(
            id = "archive-4",
            title = "Диван угловой",
            priceUsd = 175,
            priceRub = 14_650,
            location = "Мытищи, МО",
        ),
        Listing(
            id = "archive-5",
            title = "MacBook Air M2",
            priceUsd = 890,
            priceRub = 74_500,
            location = "Москва",
            isFavorite = true,
        ),
    )
}
