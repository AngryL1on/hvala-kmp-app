package tech.appard.hvala.shared.feature.favorites.presentation.viewmodels

import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ObserveListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase
import tech.appard.hvala.shared.feature.listings.domain.model.filteredBy
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.listings.presentation.mapper.sortedByUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toRegionsByCountryUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toCategoriesUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toDomain
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toListingsUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toLocationsUi
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingSortOrder
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.feature.listings.presentation.model.UILocationOption
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

data class FavoritesUiState(
    val allListings: List<UIListing> = emptyList(),
    val listings: List<UIListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isFilterSheetVisible: Boolean = false,
    val appliedFilters: UIListingsFilters = UIListingsFilters(),
    val draftFilters: UIListingsFilters = UIListingsFilters(),
    val sortOrder: UIListingSortOrder = UIListingSortOrder.NewestFirst,
    val draftSortOrder: UIListingSortOrder = UIListingSortOrder.NewestFirst,
    val categories: List<UIListingCategory> = emptyList(),
    val countries: List<UILocationOption> = emptyList(),
    val regionsByCountry: Map<String, List<UILocationOption>> = emptyMap(),
) : MviState {
    val hasAnyFavorites: Boolean
        get() = allListings.isNotEmpty()

    val availableRegions: List<UILocationOption>
        get() = draftFilters.countryId?.let { regionsByCountry[it] }.orEmpty()
}

sealed interface FavoritesIntent : MviIntent {
    data object Load : FavoritesIntent
    data object Refresh : FavoritesIntent
    data object Reset : FavoritesIntent
    data class FavoriteToggled(val listingId: String) : FavoritesIntent
    data class SortOrderChanged(val sortOrder: UIListingSortOrder) : FavoritesIntent
    data object FilterClicked : FavoritesIntent
    data object FilterDismissed : FavoritesIntent
    data class DraftFiltersChanged(val filters: UIListingsFilters) : FavoritesIntent
    data class DraftSortOrderChanged(val sortOrder: UIListingSortOrder) : FavoritesIntent
    data object FilterReset : FavoritesIntent
    data object FilterApplied : FavoritesIntent
}

sealed interface FavoritesEffect : MviEffect

class FavoritesViewModel(
    private val observeListingsUseCase: ObserveListingsUseCase,
    private val getCatalogDefaultsUseCase: GetCatalogDefaultsUseCase,
    private val toggleListingFavoriteUseCase: ToggleListingFavoriteUseCase,
    private val localeRepository: LocaleRepository,
) : MviViewModel<FavoritesIntent, FavoritesUiState, FavoritesEffect>(FavoritesUiState()) {

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
                val favorites = allListings
                    .filter { it.isFavorite && it.id.startsWith("listing-") }
                    .toListingsUi()
                updateState { current ->
                    current.copy(
                        allListings = favorites,
                        listings = applyDisplayOptions(
                            listings = favorites,
                            filters = current.appliedFilters,
                            sortOrder = current.sortOrder,
                        ),
                    )
                }
            }
        }
    }

    override suspend fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.Load -> performLoad()
            FavoritesIntent.Refresh -> performRefresh()
            FavoritesIntent.Reset -> updateState { FavoritesUiState() }
            is FavoritesIntent.FavoriteToggled -> toggleListingFavoriteUseCase(intent.listingId)
            is FavoritesIntent.SortOrderChanged -> updateState { current ->
                current.copy(
                    sortOrder = intent.sortOrder,
                    listings = applyDisplayOptions(current.allListings, current.appliedFilters, intent.sortOrder),
                )
            }
            FavoritesIntent.FilterClicked -> updateState {
                it.copy(isFilterSheetVisible = true, draftFilters = it.appliedFilters, draftSortOrder = it.sortOrder)
            }
            FavoritesIntent.FilterDismissed -> updateState { it.copy(isFilterSheetVisible = false) }
            is FavoritesIntent.DraftFiltersChanged -> updateState { it.copy(draftFilters = intent.filters) }
            is FavoritesIntent.DraftSortOrderChanged -> updateState { it.copy(draftSortOrder = intent.sortOrder) }
            FavoritesIntent.FilterReset -> updateState {
                it.copy(draftFilters = UIListingsFilters(), draftSortOrder = UIListingSortOrder.NewestFirst)
            }
            FavoritesIntent.FilterApplied -> updateState { current ->
                current.copy(
                    isFilterSheetVisible = false,
                    appliedFilters = current.draftFilters,
                    sortOrder = current.draftSortOrder,
                    listings = applyDisplayOptions(current.allListings, current.draftFilters, current.draftSortOrder),
                )
            }
        }
    }

    fun reset() = onIntent(FavoritesIntent.Reset)
    fun load() = onIntent(FavoritesIntent.Load)
    fun refresh() = onIntent(FavoritesIntent.Refresh)
    fun onListingFavoriteToggle(listingId: String) = onIntent(FavoritesIntent.FavoriteToggled(listingId))
    fun onSortOrderChange(sortOrder: UIListingSortOrder) = onIntent(FavoritesIntent.SortOrderChanged(sortOrder))
    fun onFilterClick() = onIntent(FavoritesIntent.FilterClicked)
    fun onFilterDismiss() = onIntent(FavoritesIntent.FilterDismissed)
    fun onDraftFiltersChange(filters: UIListingsFilters) = onIntent(FavoritesIntent.DraftFiltersChanged(filters))
    fun onDraftSortOrderChange(sortOrder: UIListingSortOrder) = onIntent(FavoritesIntent.DraftSortOrderChanged(sortOrder))
    fun onFilterReset() = onIntent(FavoritesIntent.FilterReset)
    fun onFilterApply() = onIntent(FavoritesIntent.FilterApplied)

    private suspend fun performLoad() {
        if (currentState().allListings.isNotEmpty() || currentState().isLoading) return
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

    private fun applyDisplayOptions(
        listings: List<UIListing>,
        filters: UIListingsFilters,
        sortOrder: UIListingSortOrder,
    ): List<UIListing> = listings
        .map { it.toDomain() }
        .filteredBy(filters.toDomain())
        .toListingsUi()
        .sortedByUi(sortOrder, filters.currency)
}

