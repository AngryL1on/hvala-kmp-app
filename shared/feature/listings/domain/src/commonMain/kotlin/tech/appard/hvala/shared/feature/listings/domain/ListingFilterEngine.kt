package tech.appard.hvala.shared.feature.listings.domain

import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCurrency
import tech.appard.hvala.shared.feature.listings.domain.model.ListingsFilters

object ListingFilterEngine {
    fun apply(
        listings: List<Listing>,
        filters: ListingsFilters,
        searchQuery: String = "",
        selectedCategoryId: String? = null,
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
}
