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
import tech.appard.hvala.shared.core.contracts.model.ListingCategory
import tech.appard.hvala.shared.core.contracts.model.ListingFilterDefaults
import tech.appard.hvala.shared.core.contracts.model.ListingSortOrder
import tech.appard.hvala.shared.core.contracts.model.ListingsFilters
import tech.appard.hvala.shared.core.contracts.model.LocationOption
import tech.appard.hvala.shared.core.contracts.model.filteredBy
import tech.appard.hvala.shared.core.contracts.model.sortedBy

data class FavoritesUiState(
    val allListings: List<Listing> = emptyList(),
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val isFilterSheetVisible: Boolean = false,
    val appliedFilters: ListingsFilters = ListingsFilters(),
    val draftFilters: ListingsFilters = ListingsFilters(),
    val sortOrder: ListingSortOrder = ListingSortOrder.NewestFirst,
    val draftSortOrder: ListingSortOrder = ListingSortOrder.NewestFirst,
    val categories: List<ListingCategory> = emptyList(),
    val countries: List<LocationOption> = emptyList(),
    val regionsByCountry: Map<String, List<LocationOption>> = emptyMap(),
) {
    val hasAnyFavorites: Boolean
        get() = allListings.isNotEmpty()

    val availableRegions: List<LocationOption>
        get() = draftFilters.countryId?.let { regionsByCountry[it] }.orEmpty()
}

class FavoritesStateHolder {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    fun reset() {
        _state.value = FavoritesUiState()
    }

    fun load() {
        if (_state.value.allListings.isNotEmpty() || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val allListings = mockFavoriteListings()
            _state.update { current ->
                current.copy(
                    isLoading = false,
                    categories = ListingFilterDefaults.categories(),
                    countries = ListingFilterDefaults.countries(),
                    regionsByCountry = ListingFilterDefaults.regionsByCountry(),
                    allListings = allListings,
                    listings = applyDisplayOptions(
                        listings = allListings,
                        filters = current.appliedFilters,
                        sortOrder = current.sortOrder,
                    ),
                )
            }
        }
    }

    fun onListingFavoriteToggle(listingId: String) {
        _state.update { current ->
            val allListings = current.allListings
                .map { listing ->
                    if (listing.id == listingId) {
                        listing.copy(isFavorite = !listing.isFavorite)
                    } else {
                        listing
                    }
                }
                .filter { it.isFavorite }

            current.copy(
                allListings = allListings,
                listings = applyDisplayOptions(
                    listings = allListings,
                    filters = current.appliedFilters,
                    sortOrder = current.sortOrder,
                ),
            )
        }
    }

    fun onSortOrderChange(sortOrder: ListingSortOrder) {
        _state.update { current ->
            current.copy(
                sortOrder = sortOrder,
                listings = applyDisplayOptions(
                    listings = current.allListings,
                    filters = current.appliedFilters,
                    sortOrder = sortOrder,
                ),
            )
        }
    }

    fun onFilterClick() {
        _state.update { current ->
            current.copy(
                isFilterSheetVisible = true,
                draftFilters = current.appliedFilters,
                draftSortOrder = current.sortOrder,
            )
        }
    }

    fun onFilterDismiss() {
        _state.update { it.copy(isFilterSheetVisible = false) }
    }

    fun onDraftFiltersChange(filters: ListingsFilters) {
        _state.update { it.copy(draftFilters = filters) }
    }

    fun onDraftSortOrderChange(sortOrder: ListingSortOrder) {
        _state.update { it.copy(draftSortOrder = sortOrder) }
    }

    fun onFilterReset() {
        _state.update {
            it.copy(
                draftFilters = ListingsFilters(),
                draftSortOrder = ListingSortOrder.NewestFirst,
            )
        }
    }

    fun onFilterApply() {
        _state.update { current ->
            current.copy(
                isFilterSheetVisible = false,
                appliedFilters = current.draftFilters,
                sortOrder = current.draftSortOrder,
                listings = applyDisplayOptions(
                    listings = current.allListings,
                    filters = current.draftFilters,
                    sortOrder = current.draftSortOrder,
                ),
            )
        }
    }

    private fun applyDisplayOptions(
        listings: List<Listing>,
        filters: ListingsFilters,
        sortOrder: ListingSortOrder,
    ): List<Listing> = listings
        .filteredBy(filters)
        .sortedBy(sortOrder, filters.currency)

    private fun mockFavoriteListings(): List<Listing> {
        val categories = listOf("clothes", "auto", "electronics", "realty", "furniture")
        val locations = listOf(
            Triple("ru", "moscow", "Москва"),
            Triple("ru", "moscow_region", "Химки, МО"),
            Triple("ru", "spb", "Санкт-Петербург"),
            Triple("rs", "belgrade", "Belgrade"),
        )

        return listOf(
            favoriteListing("favorite-0", "Худи Number Nine", 150, 12_570, locations[1], categories[0]),
            favoriteListing("favorite-1", "Кроссовки Nike Air Max", 90, 7_540, locations[0], categories[0]),
            favoriteListing("favorite-2", "Пальто Burberry", 280, 23_450, locations[1], categories[0]),
            favoriteListing("favorite-3", "Сумка Louis Vuitton", 520, 43_550, locations[2], categories[0]),
            favoriteListing("favorite-4", "Часы Rolex Submariner", 8_500, 711_500, locations[0], categories[3]),
            favoriteListing("favorite-5", "Куртка Stone Island", 340, 28_480, locations[1], categories[0]),
            favoriteListing("favorite-6", "Наушники AirPods Pro", 180, 15_080, locations[1], categories[3]),
            favoriteListing("favorite-7", "Кольцо Tiffany", 1_200, 100_500, locations[0], categories[3]),
            favoriteListing("favorite-8", "BMW X5", 41_000, 3_430_000, locations[0], categories[1]),
            favoriteListing("favorite-9", "Диван угловой", 175, 14_650, locations[1], categories[4]),
        )
    }

    private fun favoriteListing(
        id: String,
        title: String,
        priceUsd: Int,
        priceRub: Int,
        location: Triple<String, String, String>,
        categoryId: String,
    ): Listing = Listing(
        id = id,
        title = title,
        priceUsd = priceUsd,
        priceRub = priceRub,
        location = location.third,
        categoryId = categoryId,
        countryId = location.first,
        regionId = location.second,
        isFavorite = true,
    )
}
