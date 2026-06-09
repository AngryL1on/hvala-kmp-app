package tech.appard.hvala.shared.feature.listings.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.feature.listings.data.mapper.toDomain
import tech.appard.hvala.shared.feature.listings.data.model.toDomainCategories
import tech.appard.hvala.shared.feature.listings.data.model.toDomainCountries
import tech.appard.hvala.shared.feature.listings.data.model.toDomainListings
import tech.appard.hvala.shared.feature.listings.data.model.toDomainRegionsByCountry
import tech.appard.hvala.shared.feature.listings.data.source.ListingsJsonDataSource
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCategory
import tech.appard.hvala.shared.feature.listings.domain.model.LocationOption
import tech.appard.hvala.shared.feature.listings.domain.repository.CatalogRepository
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

internal class JsonListingsRepository(
    private val dataSource: ListingsJsonDataSource,
    private val localeRepository: LocaleRepository,
) : ListingsRepository {
    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    override val listings: StateFlow<List<Listing>> = _listings.asStateFlow()

    override suspend fun ensureLoaded() {
        if (_listings.value.isNotEmpty()) return
        val loaded = dataSource.listings().toDomainListings().map(::enrichListing)
        _listings.value = loaded
    }

    override fun getListingById(id: String): Listing? =
        _listings.value.find { it.id == id }

    override fun getListingsByIds(ids: List<String>): List<Listing> =
        ids.mapNotNull { id -> getListingById(id) }

    override fun getGridListings(): List<Listing> =
        _listings.value.filter { it.id.startsWith("listing-") }

    override fun getFavoriteListings(): List<Listing> =
        _listings.value.filter { it.isFavorite }

    override fun getActiveProfileListings(): List<Listing> =
        _listings.value.filter { it.id.startsWith("active-") }

    override fun getArchiveProfileListings(): List<Listing> =
        _listings.value.filter { it.id.startsWith("archive-") }

    override suspend fun toggleFavorite(listingId: String) {
        _listings.update { items ->
            items.map { listing ->
                if (listing.id == listingId) {
                    listing.copy(isFavorite = !listing.isFavorite)
                } else {
                    listing
                }
            }
        }
    }

    private fun enrichListing(listing: Listing): Listing {
        val strings = localeRepository.getLanguage().strings().listings
        val sellerName = listing.sellerName.ifBlank { "Alex M." }
        return listing.copy(
            sellerName = sellerName,
            phone = listing.phone.ifBlank { "+382 67 123 456" },
            description = listing.description.ifBlank { strings.defaultDescription },
            availability = listing.availability.ifBlank { strings.defaultAvailability },
            postedAt = listing.postedAt.ifBlank { strings.defaultPostedAt },
        )
    }
}

internal class JsonCatalogRepository(
    private val dataSource: ListingsJsonDataSource,
) : CatalogRepository {
    private var categories: List<ListingCategory> = emptyList()
    private var countries: List<LocationOption> = emptyList()
    private var regionsByCountry: Map<String, List<LocationOption>> = emptyMap()

    override suspend fun ensureLoaded() {
        if (categories.isNotEmpty()) return
        val defaults = dataSource.filterDefaults()
        categories = defaults.toDomainCategories()
        countries = defaults.toDomainCountries()
        regionsByCountry = defaults.toDomainRegionsByCountry()
    }

    override fun getCategories(): List<ListingCategory> = categories

    override fun getCountries(): List<LocationOption> = countries

    override fun getRegionsByCountry(): Map<String, List<LocationOption>> = regionsByCountry
}
