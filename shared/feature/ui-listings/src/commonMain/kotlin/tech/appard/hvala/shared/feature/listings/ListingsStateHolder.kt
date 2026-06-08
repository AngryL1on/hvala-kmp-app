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
import tech.appard.hvala.shared.core.contracts.model.ListingCurrency
import tech.appard.hvala.shared.core.contracts.model.ListingsFilters
import tech.appard.hvala.shared.core.contracts.model.ListingMockCatalog
import tech.appard.hvala.shared.core.contracts.model.LocationOption

data class ListingsUiState(
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val categories: List<ListingCategory> = emptyList(),
    val allListings: List<Listing> = emptyList(),
    val listings: List<Listing> = emptyList(),
    val isLoading: Boolean = false,
    val isFilterSheetVisible: Boolean = false,
    val appliedFilters: ListingsFilters = ListingsFilters(),
    val draftFilters: ListingsFilters = ListingsFilters(),
    val countries: List<LocationOption> = emptyList(),
    val regionsByCountry: Map<String, List<LocationOption>> = emptyMap(),
) {
    val availableRegions: List<LocationOption>
        get() = draftFilters.countryId?.let { regionsByCountry[it] }.orEmpty()
}

class ListingsStateHolder {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(ListingsUiState())
    val state: StateFlow<ListingsUiState> = _state.asStateFlow()

    fun load() {
        if (_state.value.allListings.isNotEmpty() || _state.value.isLoading) return
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            val allListings = ListingMockCatalog.allListings()
            _state.update {
                it.copy(
                    isLoading = false,
                    categories = mockCategories(),
                    countries = mockCountries(),
                    regionsByCountry = mockRegionsByCountry(),
                    allListings = allListings,
                    listings = applyFilters(allListings, it.appliedFilters, it.searchQuery, it.selectedCategoryId),
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { current ->
            current.copy(
                searchQuery = query,
                listings = applyFilters(
                    listings = current.allListings,
                    filters = current.appliedFilters,
                    searchQuery = query,
                    selectedCategoryId = current.selectedCategoryId,
                ),
            )
        }
    }

    fun onCategorySelected(categoryId: String) {
        _state.update { current ->
            val newCategoryId = if (current.selectedCategoryId == categoryId) null else categoryId
            current.copy(
                selectedCategoryId = newCategoryId,
                listings = applyFilters(
                    listings = current.allListings,
                    filters = current.appliedFilters,
                    searchQuery = current.searchQuery,
                    selectedCategoryId = newCategoryId,
                ),
            )
        }
    }

    fun onListingFavoriteToggle(listingId: String) {
        _state.update { current ->
            val updateListings = { listings: List<Listing> ->
                listings.map { listing ->
                    if (listing.id == listingId) {
                        listing.copy(isFavorite = !listing.isFavorite)
                    } else {
                        listing
                    }
                }
            }
            val allListings = updateListings(current.allListings)
            current.copy(
                allListings = allListings,
                listings = applyFilters(
                    listings = allListings,
                    filters = current.appliedFilters,
                    searchQuery = current.searchQuery,
                    selectedCategoryId = current.selectedCategoryId,
                ),
            )
        }
    }

    fun onFilterClick() {
        _state.update { current ->
            current.copy(
                isFilterSheetVisible = true,
                draftFilters = current.appliedFilters,
            )
        }
    }

    fun onFilterDismiss() {
        _state.update { it.copy(isFilterSheetVisible = false) }
    }

    fun onDraftFiltersChange(filters: ListingsFilters) {
        _state.update { it.copy(draftFilters = filters) }
    }

    fun onFilterReset() {
        _state.update { it.copy(draftFilters = ListingsFilters()) }
    }

    fun onFilterApply() {
        _state.update { current ->
            val appliedFilters = current.draftFilters
            current.copy(
                isFilterSheetVisible = false,
                appliedFilters = appliedFilters,
                selectedCategoryId = appliedFilters.categoryId,
                listings = applyFilters(
                    listings = current.allListings,
                    filters = appliedFilters,
                    searchQuery = current.searchQuery,
                    selectedCategoryId = appliedFilters.categoryId,
                ),
            )
        }
    }

    private fun applyFilters(
        listings: List<Listing>,
        filters: ListingsFilters,
        searchQuery: String,
        selectedCategoryId: String?,
    ): List<Listing> {
        val query = searchQuery.trim()
        val minPrice = filters.minPrice.toIntOrNull()
        val maxPrice = filters.maxPrice.toIntOrNull()
        val categoryId = filters.categoryId ?: selectedCategoryId

        return listings.filter { listing ->
            val listingPrice = when (filters.currency) {
                ListingCurrency.USD -> listing.priceUsd
                ListingCurrency.RUB -> listing.priceRub
            }
            val matchesQuery = query.isEmpty() ||
                listing.title.contains(query, ignoreCase = true) ||
                listing.location.contains(query, ignoreCase = true)
            val matchesCategory = categoryId == null || listing.categoryId == categoryId
            val matchesCountry = filters.countryId == null || listing.countryId == filters.countryId
            val matchesRegion = filters.regionId == null || listing.regionId == filters.regionId
            val matchesMinPrice = minPrice == null || listingPrice >= minPrice
            val matchesMaxPrice = maxPrice == null || listingPrice <= maxPrice

            matchesQuery && matchesCategory && matchesCountry && matchesRegion &&
                matchesMinPrice && matchesMaxPrice
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

    private fun mockCountries(): List<LocationOption> = listOf(
        LocationOption(id = "ru", title = "Россия"),
        LocationOption(id = "rs", title = "Сербия"),
    )

    private fun mockRegionsByCountry(): Map<String, List<LocationOption>> = mapOf(
        "ru" to listOf(
            LocationOption(id = "moscow", title = "Москва"),
            LocationOption(id = "moscow_region", title = "Московская область"),
            LocationOption(id = "spb", title = "Санкт-Петербург"),
        ),
        "rs" to listOf(
            LocationOption(id = "belgrade", title = "Belgrade"),
            LocationOption(id = "sumadija", title = "Šumadija and Western Serbia"),
        ),
    )
}
