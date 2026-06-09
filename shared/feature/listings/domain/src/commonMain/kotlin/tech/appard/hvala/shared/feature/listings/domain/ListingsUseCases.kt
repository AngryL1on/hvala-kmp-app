package tech.appard.hvala.shared.feature.listings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.repository.CatalogRepository
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository

class ObserveListingsUseCase(
    private val listingsRepository: ListingsRepository,
    private val authRepository: AuthRepository,
) {
    val listingsFlow: Flow<List<Listing>> = combine(
        listingsRepository.listings,
        authRepository.isAuthenticatedFlow,
    ) { listings, isAuthenticated ->
        listings.withFavoriteStateVisible(isAuthenticated)
    }

    suspend operator fun invoke(): List<Listing> {
        listingsRepository.ensureLoaded()
        return listingsRepository.getGridListings()
            .withFavoriteStateVisible(authRepository.isAuthenticated())
    }
}

class GetListingByIdUseCase(
    private val listingsRepository: ListingsRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(listingId: String): Listing? {
        listingsRepository.ensureLoaded()
        return listingsRepository.getListingById(listingId)
            ?.withFavoriteStateVisible(authRepository.isAuthenticated())
    }
}

class ToggleListingFavoriteUseCase(
    private val listingsRepository: ListingsRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(listingId: String) {
        if (!authRepository.isAuthenticated()) return
        listingsRepository.ensureLoaded()
        listingsRepository.toggleFavorite(listingId)
    }
}

class GetFavoriteListingsUseCase(
    private val listingsRepository: ListingsRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): List<Listing> {
        if (!authRepository.isAuthenticated()) return emptyList()
        listingsRepository.ensureLoaded()
        return listingsRepository.getFavoriteListings()
            .filter { it.id.startsWith("listing-") }
    }
}

class GetCatalogDefaultsUseCase(
    private val catalogRepository: CatalogRepository,
) {
    suspend operator fun invoke() {
        catalogRepository.ensureLoaded()
    }

    fun categories() = catalogRepository.getCategories()

    fun countries() = catalogRepository.getCountries()

    fun regionsByCountry() = catalogRepository.getRegionsByCountry()
}

private fun List<Listing>.withFavoriteStateVisible(isAuthenticated: Boolean): List<Listing> =
    if (isAuthenticated) this else map { it.copy(isFavorite = false) }

private fun Listing.withFavoriteStateVisible(isAuthenticated: Boolean): Listing =
    if (isAuthenticated) this else copy(isFavorite = false)
