package tech.appard.hvala.shared.feature.listings.presentation.viewmodels

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ListingFilterEngine
import tech.appard.hvala.shared.feature.listings.domain.ObserveListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toRegionsByCountryUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toCategoriesUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toDomain
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toListingsUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toLocationsUi
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.feature.listings.presentation.model.UILocationOption
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

data class ListingsUiState(
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val categories: List<UIListingCategory> = emptyList(),
    val allListings: List<UIListing> = emptyList(),
    val listings: List<UIListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isFilterSheetVisible: Boolean = false,
    val appliedFilters: UIListingsFilters = UIListingsFilters(),
    val draftFilters: UIListingsFilters = UIListingsFilters(),
    val countries: List<UILocationOption> = emptyList(),
    val regionsByCountry: Map<String, List<UILocationOption>> = emptyMap(),
) : MviState {
    val availableRegions: List<UILocationOption>
        get() = draftFilters.countryId?.let { regionsByCountry[it] }.orEmpty()
}

sealed interface ListingsIntent : MviIntent {
    data object Load : ListingsIntent
    data object Refresh : ListingsIntent
    data class SearchQueryChanged(val query: String) : ListingsIntent
    data class CategorySelected(val categoryId: String) : ListingsIntent
    data class FavoriteToggled(val listingId: String) : ListingsIntent
    data object FilterClicked : ListingsIntent
    data object FilterDismissed : ListingsIntent
    data class DraftFiltersChanged(val filters: UIListingsFilters) : ListingsIntent
    data object FilterReset : ListingsIntent
    data object FilterApplied : ListingsIntent
}

sealed interface ListingsEffect : MviEffect

class ListingsViewModel(
    private val observeListingsUseCase: ObserveListingsUseCase,
    private val getCatalogDefaultsUseCase: GetCatalogDefaultsUseCase,
    private val toggleListingFavoriteUseCase: ToggleListingFavoriteUseCase,
    private val localeRepository: LocaleRepository,
) : MviViewModel<ListingsIntent, ListingsUiState, ListingsEffect>(ListingsUiState()) {

    init {
        viewModelScope.launch {
            localeRepository.languageFlow.collectLatest { language ->
                getCatalogDefaultsUseCase()
                updateState { current ->
                    current.copy(
                        categories = getCatalogDefaultsUseCase.categories().toCategoriesUi(language),
                        countries = getCatalogDefaultsUseCase.countries().toLocationsUi(language),
                        regionsByCountry = getCatalogDefaultsUseCase.regionsByCountry()
                            .toRegionsByCountryUi(language),
                    )
                }
            }
        }
        viewModelScope.launch {
            observeListingsUseCase.listingsFlow.collectLatest { allListings ->
                val gridListings = allListings.filter { it.id.startsWith("listing-") }.toListingsUi()
                updateState { current ->
                    current.copy(
                        allListings = gridListings,
                        listings = applyFilters(
                            listings = gridListings,
                            filters = current.appliedFilters,
                            searchQuery = current.searchQuery,
                            selectedCategoryId = current.selectedCategoryId,
                        ),
                    )
                }
            }
        }
    }

    override suspend fun handleIntent(intent: ListingsIntent) {
        when (intent) {
            ListingsIntent.Load -> performLoad()
            ListingsIntent.Refresh -> performRefresh()
            is ListingsIntent.SearchQueryChanged -> applySearchQueryChange(intent.query)
            is ListingsIntent.CategorySelected -> applyCategorySelected(intent.categoryId)
            is ListingsIntent.FavoriteToggled -> toggleListingFavoriteUseCase(intent.listingId)
            ListingsIntent.FilterClicked -> updateState {
                it.copy(isFilterSheetVisible = true, draftFilters = it.appliedFilters)
            }
            ListingsIntent.FilterDismissed -> updateState { it.copy(isFilterSheetVisible = false) }
            is ListingsIntent.DraftFiltersChanged -> updateState { it.copy(draftFilters = intent.filters) }
            ListingsIntent.FilterReset -> updateState { it.copy(draftFilters = UIListingsFilters()) }
            ListingsIntent.FilterApplied -> applyFiltersIntent()
        }
    }

    fun load() = onIntent(ListingsIntent.Load)

    fun refresh() = onIntent(ListingsIntent.Refresh)

    fun onSearchQueryChange(query: String) = onIntent(ListingsIntent.SearchQueryChanged(query))

    fun onCategorySelected(categoryId: String) = onIntent(ListingsIntent.CategorySelected(categoryId))

    fun onListingFavoriteToggle(listingId: String) = onIntent(ListingsIntent.FavoriteToggled(listingId))

    fun onFilterClick() = onIntent(ListingsIntent.FilterClicked)

    fun onFilterDismiss() = onIntent(ListingsIntent.FilterDismissed)

    fun onDraftFiltersChange(filters: UIListingsFilters) = onIntent(ListingsIntent.DraftFiltersChanged(filters))

    fun onFilterReset() = onIntent(ListingsIntent.FilterReset)

    fun onFilterApply() = onIntent(ListingsIntent.FilterApplied)

    private suspend fun performLoad() {
        if (currentState().categories.isNotEmpty() && currentState().allListings.isNotEmpty()) return
        if (currentState().isLoading) return
        updateState { it.copy(isLoading = true) }
        getCatalogDefaultsUseCase()
        observeListingsUseCase()
        val language = localeRepository.getLanguage()
        updateState {
            it.copy(
                isLoading = false,
                categories = getCatalogDefaultsUseCase.categories().toCategoriesUi(language),
                countries = getCatalogDefaultsUseCase.countries().toLocationsUi(language),
                regionsByCountry = getCatalogDefaultsUseCase.regionsByCountry()
                    .toRegionsByCountryUi(language),
            )
        }
    }

    private suspend fun performRefresh() {
        if (currentState().isRefreshing) return
        updateState { it.copy(isRefreshing = true) }
        observeListingsUseCase.refresh()
        updateState { it.copy(isRefreshing = false) }
    }

    private fun applySearchQueryChange(query: String) {
        updateState { current ->
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

    private fun applyCategorySelected(categoryId: String) {
        updateState { current ->
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

    private fun applyFiltersIntent() {
        updateState { current ->
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
        listings: List<UIListing>,
        filters: UIListingsFilters,
        searchQuery: String,
        selectedCategoryId: String?,
    ): List<UIListing> = ListingFilterEngine.apply(
        listings = listings.map { it.toDomain() },
        filters = filters.toDomain(),
        searchQuery = searchQuery,
        selectedCategoryId = selectedCategoryId,
    ).toListingsUi()
}

