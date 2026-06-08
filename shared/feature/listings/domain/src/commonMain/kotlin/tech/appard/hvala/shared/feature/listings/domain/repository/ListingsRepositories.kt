package tech.appard.hvala.shared.feature.listings.domain.repository

import kotlinx.coroutines.flow.StateFlow
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCategory
import tech.appard.hvala.shared.feature.listings.domain.model.LocationOption

interface ListingsRepository {
    val listings: StateFlow<List<Listing>>

    suspend fun ensureLoaded()

    fun getListingById(id: String): Listing?

    fun getListingsByIds(ids: List<String>): List<Listing>

    fun getGridListings(): List<Listing>

    fun getFavoriteListings(): List<Listing>

    fun getActiveProfileListings(): List<Listing>

    fun getArchiveProfileListings(): List<Listing>

    suspend fun toggleFavorite(listingId: String)
}

interface CatalogRepository {
    suspend fun ensureLoaded()

    fun getCategories(): List<ListingCategory>

    fun getCountries(): List<LocationOption>

    fun getRegionsByCountry(): Map<String, List<LocationOption>>
}
