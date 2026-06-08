package tech.appard.hvala.shared.feature.listings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.Listing
import tech.appard.hvala.shared.core.contracts.model.ListingCategory

data class ListingsUiState(
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val categories: List<ListingCategory> = emptyList(),
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
)

class ListingsStateHolder {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(ListingsUiState())
    val state: StateFlow<ListingsUiState> = _state.asStateFlow()

    fun load() {
        if (_state.value.listings.isNotEmpty() || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.update {
                it.copy(
                    isLoading = false,
                    categories = mockCategories(),
                    listings = mockListings(),
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(categoryId: String) {
        _state.update { current ->
            current.copy(
                selectedCategoryId = if (current.selectedCategoryId == categoryId) null else categoryId,
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

    private fun mockCategories(): List<ListingCategory> = listOf(
        ListingCategory(id = "clothes", title = "Одежда"),
        ListingCategory(id = "auto", title = "Авто"),
        ListingCategory(id = "realty", title = "Недвижимость"),
        ListingCategory(id = "electronics", title = "Электроника"),
        ListingCategory(id = "furniture", title = "Мебель"),
        ListingCategory(id = "jobs", title = "Работа"),
        ListingCategory(id = "services", title = "Услуги"),
        ListingCategory(id = "hobby", title = "Хобби"),
        ListingCategory(id = "kids", title = "Детям"),
        ListingCategory(id = "pets", title = "Животные"),
    )

    private fun mockListings(): List<Listing> = List(24) { index ->
        Listing(
            id = "listing-$index",
            title = "Худи Number Nine",
            priceUsd = 150,
            priceRub = 12_570,
            location = "Химки, МО",
        )
    }
}
