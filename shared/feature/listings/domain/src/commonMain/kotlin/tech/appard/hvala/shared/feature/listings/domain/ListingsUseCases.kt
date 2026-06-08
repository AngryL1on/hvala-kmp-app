package tech.appard.hvala.shared.feature.listings.domain

import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.repository.CatalogRepository
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository

class ObserveListingsUseCase(
    private val listingsRepository: ListingsRepository,
) {
    val listingsFlow = listingsRepository.listings

    suspend operator fun invoke(): List<Listing> {
        listingsRepository.ensureLoaded()
        return listingsRepository.getGridListings()
    }
}

class GetListingByIdUseCase(
    private val listingsRepository: ListingsRepository,
) {
    suspend operator fun invoke(listingId: String): Listing? {
        listingsRepository.ensureLoaded()
        return listingsRepository.getListingById(listingId)
    }
}

class ToggleListingFavoriteUseCase(
    private val listingsRepository: ListingsRepository,
) {
    suspend operator fun invoke(listingId: String) {
        listingsRepository.ensureLoaded()
        listingsRepository.toggleFavorite(listingId)
    }
}

class GetFavoriteListingsUseCase(
    private val listingsRepository: ListingsRepository,
) {
    suspend operator fun invoke(): List<Listing> {
        listingsRepository.ensureLoaded()
        return listingsRepository.getFavoriteListings()
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
