package tech.appard.hvala.shared.feature.listings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.contracts.model.ListingFilterDefaults
import tech.appard.hvala.shared.core.contracts.model.ListingMockCatalog

data class ListingDetailUiState(
    val listing: Listing? = null,
    val isLoading: Boolean = false,
    val currentPhotoIndex: Int = 0,
    val categoryTitle: String = "",
    val countryTitle: String = "",
    val regionTitle: String = "",
    val error: String? = null,
)

class ListingDetailStateHolder {
    private val _state = MutableStateFlow(ListingDetailUiState())
    val state: StateFlow<ListingDetailUiState> = _state.asStateFlow()

    fun load(listingId: String, isFavoriteOverride: Boolean? = null) {
        _state.update { it.copy(isLoading = true, error = null) }

        val listing = ListingMockCatalog.listingById(listingId)
        if (listing == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    listing = null,
                    error = "Listing not found",
                )
            }
            return
        }

        val resolvedFavorite = isFavoriteOverride ?: listing.isFavorite
        val enriched = listing.copy(isFavorite = resolvedFavorite)

        _state.update {
            it.copy(
                isLoading = false,
                listing = enriched,
                currentPhotoIndex = 0,
                categoryTitle = resolveCategoryTitle(enriched.categoryId),
                countryTitle = resolveCountryTitle(enriched.countryId),
                regionTitle = resolveRegionTitle(enriched.countryId, enriched.regionId),
                error = null,
            )
        }
    }

    fun onPhotoSelected(index: Int) {
        _state.update { it.copy(currentPhotoIndex = index) }
    }

    fun onFavoriteToggle() {
        _state.update { current ->
            val listing = current.listing ?: return@update current
            current.copy(listing = listing.copy(isFavorite = !listing.isFavorite))
        }
    }

    fun syncFavorite(isFavorite: Boolean) {
        _state.update { current ->
            val listing = current.listing ?: return@update current
            current.copy(listing = listing.copy(isFavorite = isFavorite))
        }
    }

    fun reset() {
        _state.value = ListingDetailUiState()
    }

    private fun resolveCategoryTitle(categoryId: String): String =
        ListingFilterDefaults.categories().find { it.id == categoryId }?.title.orEmpty()

    private fun resolveCountryTitle(countryId: String): String =
        ListingFilterDefaults.countries().find { it.id == countryId }?.title.orEmpty()

    private fun resolveRegionTitle(countryId: String, regionId: String): String =
        ListingFilterDefaults.regionsByCountry()[countryId]
            ?.find { it.id == regionId }
            ?.title
            .orEmpty()
}
