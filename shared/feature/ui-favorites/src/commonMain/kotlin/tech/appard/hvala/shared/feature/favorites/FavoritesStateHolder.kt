package tech.appard.hvala.shared.feature.favorites

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.Listing

data class FavoritesUiState(
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
)

class FavoritesStateHolder {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    fun reset() {
        _state.value = FavoritesUiState()
    }

    fun load() {
        if (_state.value.listings.isNotEmpty() || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            _state.update {
                it.copy(
                    isLoading = false,
                    listings = mockFavoriteListings(),
                )
            }
        }
    }

    fun onListingFavoriteToggle(listingId: String) {
        _state.update { current ->
            current.copy(
                listings = current.listings
                    .map { listing ->
                        if (listing.id == listingId) {
                            listing.copy(isFavorite = !listing.isFavorite)
                        } else {
                            listing
                        }
                    }
                    .filter { it.isFavorite },
            )
        }
    }

    private fun mockFavoriteListings(): List<Listing> = listOf(
        Listing(
            id = "favorite-0",
            title = "Худи Number Nine",
            priceUsd = 150,
            priceRub = 12_570,
            location = "Химки, МО",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-1",
            title = "Кроссовки Nike Air Max",
            priceUsd = 90,
            priceRub = 7_540,
            location = "Москва",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-2",
            title = "Пальто Burberry",
            priceUsd = 280,
            priceRub = 23_450,
            location = "Химки, МО",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-3",
            title = "Сумка Louis Vuitton",
            priceUsd = 520,
            priceRub = 43_550,
            location = "Санкт-Петербург",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-4",
            title = "Часы Rolex Submariner",
            priceUsd = 8_500,
            priceRub = 711_500,
            location = "Москва",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-5",
            title = "Куртка Stone Island",
            priceUsd = 340,
            priceRub = 28_480,
            location = "Красногорск, МО",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-6",
            title = "Наушники AirPods Pro",
            priceUsd = 180,
            priceRub = 15_080,
            location = "Мытищи, МО",
            isFavorite = true,
        ),
        Listing(
            id = "favorite-7",
            title = "Кольцо Tiffany",
            priceUsd = 1_200,
            priceRub = 100_500,
            location = "Москва",
            isFavorite = true,
        ),
    )
}
