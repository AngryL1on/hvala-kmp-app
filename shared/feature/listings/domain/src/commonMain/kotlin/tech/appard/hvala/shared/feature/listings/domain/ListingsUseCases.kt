package tech.appard.hvala.shared.feature.listings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository
import tech.appard.hvala.shared.feature.listings.domain.model.CreateListingDraft
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCurrency
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

    suspend fun refresh() {
        listingsRepository.refresh()
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

class CreateListingUseCase(
    private val listingsRepository: ListingsRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(draft: CreateListingDraft): Listing {
        check(authRepository.isAuthenticated()) { "Authentication required" }
        listingsRepository.ensureLoaded()
        val profile = profileRepository.getCurrentProfile()
        val sellerId = draft.sellerId.ifBlank { profile.id }
        val sellerName = draft.sellerName.ifBlank { profile.fullName }
        return listingsRepository.createListing(
            draft.copy(
                sellerId = sellerId,
                sellerName = sellerName,
            ),
        )
    }
}

private const val USD_TO_RUB_RATE = 84

fun pricePair(price: Int, currency: ListingCurrency): Pair<Int, Int> = when (currency) {
    ListingCurrency.USD -> price to price * USD_TO_RUB_RATE
    ListingCurrency.RUB -> (price / USD_TO_RUB_RATE) to price
}

private fun List<Listing>.withFavoriteStateVisible(isAuthenticated: Boolean): List<Listing> =
    if (isAuthenticated) this else map { it.copy(isFavorite = false) }

private fun Listing.withFavoriteStateVisible(isAuthenticated: Boolean): Listing =
    if (isAuthenticated) this else copy(isFavorite = false)
