package tech.appard.hvala.shared.feature.profile.presentation.viewmodels

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.feature.profile.domain.GetSellerProfileUseCase
import tech.appard.hvala.shared.feature.listings.domain.ObserveListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toListingsUi
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.profile.presentation.mapper.toUi
import tech.appard.hvala.shared.feature.profile.presentation.model.UISellerProfile

data class SellerProfileUiState(
    val seller: UISellerProfile? = null,
    val listings: List<UIListing> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) : MviState

sealed interface SellerProfileIntent : MviIntent {
    data class Load(val sellerId: String) : SellerProfileIntent
    data object Reset : SellerProfileIntent
    data class FavoriteToggled(val listingId: String) : SellerProfileIntent
}

sealed interface SellerProfileEffect : MviEffect

class SellerProfileViewModel(
    private val getSellerProfileUseCase: GetSellerProfileUseCase,
    private val observeListingsUseCase: ObserveListingsUseCase,
    private val toggleListingFavoriteUseCase: ToggleListingFavoriteUseCase,
) : MviViewModel<SellerProfileIntent, SellerProfileUiState, SellerProfileEffect>(SellerProfileUiState()) {

    private var currentSellerId: String? = null

    init {
        viewModelScope.launch {
            observeListingsUseCase.listingsFlow.collectLatest {
                val sellerId = currentSellerId ?: return@collectLatest
                reloadSeller(sellerId)
            }
        }
    }

    override suspend fun handleIntent(intent: SellerProfileIntent) {
        when (intent) {
            is SellerProfileIntent.Load -> performLoad(intent.sellerId)
            SellerProfileIntent.Reset -> {
                currentSellerId = null
                updateState { SellerProfileUiState() }
            }
            is SellerProfileIntent.FavoriteToggled -> toggleListingFavoriteUseCase(intent.listingId)
        }
    }

    fun load(sellerId: String) = onIntent(SellerProfileIntent.Load(sellerId))
    fun reset() = onIntent(SellerProfileIntent.Reset)
    fun onListingFavoriteToggle(listingId: String) = onIntent(SellerProfileIntent.FavoriteToggled(listingId))

    private suspend fun performLoad(sellerId: String) {
        currentSellerId = sellerId
        updateState { it.copy(isLoading = true, error = null) }
        reloadSeller(sellerId)
    }

    private suspend fun reloadSeller(sellerId: String) {
        val bundle = getSellerProfileUseCase(sellerId)
        if (bundle == null) {
            updateState { it.copy(isLoading = false, seller = null, listings = emptyList(), error = "Seller not found") }
            return
        }
        updateState {
            it.copy(
                isLoading = false,
                seller = bundle.seller.toUi(),
                listings = bundle.listings.toListingsUi(),
                error = null,
            )
        }
    }
}

typealias SellerProfileStateHolder = SellerProfileViewModel
