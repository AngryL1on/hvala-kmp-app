package tech.appard.hvala.shared.feature.listings.presentation.viewmodels

import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository
import tech.appard.hvala.shared.feature.listings.domain.GetListingByIdUseCase
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toUi
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing

data class ListingDetailUiState(
    val listing: UIListing? = null,
    val isLoading: Boolean = false,
    val currentPhotoIndex: Int = 0,
    val categoryTitle: String = "",
    val countryTitle: String = "",
    val regionTitle: String = "",
    val error: String? = null,
) : MviState

sealed interface ListingDetailIntent : MviIntent {
    data class Load(val listingId: String, val isFavoriteOverride: Boolean? = null) : ListingDetailIntent
    data class PhotoSelected(val index: Int) : ListingDetailIntent
    data object FavoriteToggled : ListingDetailIntent
    data class SyncFavorite(val isFavorite: Boolean) : ListingDetailIntent
    data object Reset : ListingDetailIntent
}

sealed interface ListingDetailEffect : MviEffect

class ListingDetailViewModel(
    private val getListingByIdUseCase: GetListingByIdUseCase,
    private val getCatalogDefaultsUseCase: GetCatalogDefaultsUseCase,
    private val localeRepository: LocaleRepository,
) : MviViewModel<ListingDetailIntent, ListingDetailUiState, ListingDetailEffect>(ListingDetailUiState()) {

    override suspend fun handleIntent(intent: ListingDetailIntent) {
        when (intent) {
            is ListingDetailIntent.Load -> performLoad(intent.listingId, intent.isFavoriteOverride)
            is ListingDetailIntent.PhotoSelected -> updateState { it.copy(currentPhotoIndex = intent.index) }
            ListingDetailIntent.FavoriteToggled -> updateState { current ->
                val listing = current.listing ?: return@updateState current
                current.copy(listing = listing.copy(isFavorite = !listing.isFavorite))
            }
            is ListingDetailIntent.SyncFavorite -> updateState { current ->
                val listing = current.listing ?: return@updateState current
                current.copy(listing = listing.copy(isFavorite = intent.isFavorite))
            }
            ListingDetailIntent.Reset -> updateState { ListingDetailUiState() }
        }
    }

    fun load(listingId: String, isFavoriteOverride: Boolean? = null) =
        onIntent(ListingDetailIntent.Load(listingId, isFavoriteOverride))

    fun onPhotoSelected(index: Int) = onIntent(ListingDetailIntent.PhotoSelected(index))

    fun onFavoriteToggle() = onIntent(ListingDetailIntent.FavoriteToggled)

    fun syncFavorite(isFavorite: Boolean) = onIntent(ListingDetailIntent.SyncFavorite(isFavorite))

    fun reset() = onIntent(ListingDetailIntent.Reset)

    private suspend fun performLoad(listingId: String, isFavoriteOverride: Boolean?) {
        updateState { it.copy(isLoading = true, error = null) }
        getCatalogDefaultsUseCase()
        val listing = getListingByIdUseCase(listingId)
        if (listing == null) {
            updateState {
                it.copy(
                    isLoading = false,
                    listing = null,
                    error = localeRepository.getLanguage().strings().listings.listingNotFound,
                )
            }
            return
        }

        val resolvedFavorite = isFavoriteOverride ?: listing.isFavorite
        val enriched = listing.copy(isFavorite = resolvedFavorite)

        updateState {
            it.copy(
                isLoading = false,
                listing = enriched.toUi(),
                currentPhotoIndex = 0,
                categoryTitle = resolveCategoryTitle(enriched.categoryId),
                countryTitle = resolveCountryTitle(enriched.countryId),
                regionTitle = resolveRegionTitle(enriched.countryId, enriched.regionId),
                error = null,
            )
        }
    }

    private fun resolveCategoryTitle(categoryId: String): String {
        val strings = localeRepository.getLanguage().strings().listings
        val fallback = getCatalogDefaultsUseCase.categories().find { it.id == categoryId }?.title.orEmpty()
        return strings.categoryTitle(categoryId, fallback)
    }

    private fun resolveCountryTitle(countryId: String): String {
        val strings = localeRepository.getLanguage().strings().listings
        val fallback = getCatalogDefaultsUseCase.countries().find { it.id == countryId }?.title.orEmpty()
        return strings.countryTitle(countryId, fallback)
    }

    private fun resolveRegionTitle(countryId: String, regionId: String): String {
        val strings = localeRepository.getLanguage().strings().listings
        val fallback = getCatalogDefaultsUseCase.regionsByCountry()[countryId]
            ?.find { it.id == regionId }
            ?.title
            .orEmpty()
        return strings.regionTitle(regionId, fallback)
    }
}

typealias ListingDetailStateHolder = ListingDetailViewModel
