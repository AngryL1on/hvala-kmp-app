package tech.appard.hvala.shared.feature.profile

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository
import tech.appard.hvala.shared.feature.profile.domain.GetProfileOverviewUseCase
import tech.appard.hvala.shared.feature.listings.domain.ObserveListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.listings.ui.mapper.toListingsUi
import tech.appard.hvala.shared.feature.listings.ui.model.UIListing
import tech.appard.hvala.shared.feature.profile.ui.mapper.toUi as profileToUi
import tech.appard.hvala.shared.feature.profile.ui.model.UIUserProfile

data class ProfileUiState(
    val profile: UIUserProfile? = null,
    val isLoading: Boolean = false,
    val activeListingsCount: Int = 0,
    val rating: Float = 0f,
    val memberSince: String = "",
    val selectedTab: ProfileListingsTab = ProfileListingsTab.Active,
    val activeListings: List<UIListing> = emptyList(),
    val archiveListings: List<UIListing> = emptyList(),
) : MviState {
    val listings: List<UIListing>
        get() = when (selectedTab) {
            ProfileListingsTab.Active -> activeListings
            ProfileListingsTab.Archive -> archiveListings
        }
}

sealed interface ProfileIntent : MviIntent {
    data object Load : ProfileIntent
    data object Reset : ProfileIntent
    data class TabSelected(val tab: ProfileListingsTab) : ProfileIntent
    data class FavoriteToggled(val listingId: String) : ProfileIntent
}

sealed interface ProfileEffect : MviEffect

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val getProfileOverviewUseCase: GetProfileOverviewUseCase,
    private val observeListingsUseCase: ObserveListingsUseCase,
    private val toggleListingFavoriteUseCase: ToggleListingFavoriteUseCase,
) : MviViewModel<ProfileIntent, ProfileUiState, ProfileEffect>(ProfileUiState()) {

    init {
        viewModelScope.launch {
            observeListingsUseCase.listingsFlow.collectLatest {
                val bundle = getProfileOverviewUseCase()
                updateState { current ->
                    current.copy(
                        activeListings = bundle.activeListings.toListingsUi(),
                        archiveListings = bundle.archiveListings.toListingsUi(),
                        activeListingsCount = bundle.overview.activeListingsCount,
                        rating = bundle.overview.rating,
                        memberSince = bundle.overview.memberSince,
                    )
                }
            }
        }
    }

    override suspend fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Load -> performLoad()
            ProfileIntent.Reset -> updateState { ProfileUiState() }
            is ProfileIntent.TabSelected -> updateState { current ->
                if (current.selectedTab == intent.tab) current else current.copy(selectedTab = intent.tab)
            }
            is ProfileIntent.FavoriteToggled -> toggleListingFavoriteUseCase(intent.listingId)
        }
    }

    fun reset() = onIntent(ProfileIntent.Reset)
    fun load() = onIntent(ProfileIntent.Load)
    fun onTabSelected(tab: ProfileListingsTab) = onIntent(ProfileIntent.TabSelected(tab))
    fun onListingFavoriteToggle(listingId: String) = onIntent(ProfileIntent.FavoriteToggled(listingId))

    private suspend fun performLoad() {
        if (currentState().profile != null || currentState().isLoading) return
        updateState { it.copy(isLoading = true) }
        val profile = profileRepository.getCurrentProfile()
        val bundle = getProfileOverviewUseCase()
        updateState {
            it.copy(
                profile = profile.profileToUi(),
                isLoading = false,
                activeListingsCount = bundle.overview.activeListingsCount,
                rating = bundle.overview.rating,
                memberSince = bundle.overview.memberSince,
                activeListings = bundle.activeListings.toListingsUi(),
                archiveListings = bundle.archiveListings.toListingsUi(),
            )
        }
    }
}

typealias ProfileStateHolder = ProfileViewModel
