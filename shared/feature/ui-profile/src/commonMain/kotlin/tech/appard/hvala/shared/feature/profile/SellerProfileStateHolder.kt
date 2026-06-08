package tech.appard.hvala.shared.feature.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.contracts.model.SellerMockCatalog
import tech.appard.hvala.shared.core.contracts.model.SellerProfile

data class SellerProfileUiState(
    val seller: SellerProfile? = null,
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class SellerProfileStateHolder {
    private val _state = MutableStateFlow(SellerProfileUiState())
    val state: StateFlow<SellerProfileUiState> = _state.asStateFlow()

    fun load(sellerId: String) {
        _state.update { it.copy(isLoading = true, error = null) }

        val seller = SellerMockCatalog.sellerById(sellerId)
        if (seller == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    seller = null,
                    listings = emptyList(),
                    error = "Seller not found",
                )
            }
            return
        }

        _state.update {
            it.copy(
                isLoading = false,
                seller = seller,
                listings = SellerMockCatalog.listingsForSeller(sellerId),
                error = null,
            )
        }
    }

    fun onListingFavoriteToggle(listingId: String) {
        _state.update { current ->
            current.copy(
                listings = current.listings.map { listing ->
                    if (listing.id == listingId) {
                        listing.copy(isFavorite = !listing.isFavorite)
                    } else {
                        listing
                    }
                },
            )
        }
    }

    fun reset() {
        _state.value = SellerProfileUiState()
    }
}
