package tech.appard.hvala.shared.feature.listings.domain.model

enum class ListingCurrency(val code: String, val symbol: String) {
    USD(code = "USD", symbol = "$"),
    RUB(code = "RUB", symbol = "₽"),
}

data class LocationOption(
    val id: String,
    val title: String,
)

data class ListingsFilters(
    val currency: ListingCurrency = ListingCurrency.USD,
    val minPrice: String = "",
    val maxPrice: String = "",
    val countryId: String? = null,
    val regionId: String? = null,
    val categoryId: String? = null,
)

enum class ListingSortOrder(val title: String) {
    NewestFirst("Сначала новые"),
    PriceAsc("Цена ↑"),
    PriceDesc("Цена ↓"),
}

fun List<Listing>.filteredBy(
    filters: ListingsFilters,
    searchQuery: String = "",
): List<Listing> {
    val query = searchQuery.trim()
    val minPrice = filters.minPrice.toIntOrNull()
    val maxPrice = filters.maxPrice.toIntOrNull()

    return filter { listing ->
        val listingPrice = when (filters.currency) {
            ListingCurrency.USD -> listing.priceUsd
            ListingCurrency.RUB -> listing.priceRub
        }
        val matchesQuery = query.isEmpty() ||
            listing.title.contains(query, ignoreCase = true) ||
            listing.location.contains(query, ignoreCase = true)
        val matchesCategory = filters.categoryId == null || listing.categoryId == filters.categoryId
        val matchesCountry = filters.countryId == null || listing.countryId == filters.countryId
        val matchesRegion = filters.regionId == null || listing.regionId == filters.regionId
        val matchesMinPrice = minPrice == null || listingPrice >= minPrice
        val matchesMaxPrice = maxPrice == null || listingPrice <= maxPrice

        matchesQuery && matchesCategory && matchesCountry && matchesRegion &&
            matchesMinPrice && matchesMaxPrice
    }
}

fun List<Listing>.sortedBy(
    sortOrder: ListingSortOrder,
    currency: ListingCurrency,
): List<Listing> = when (sortOrder) {
    ListingSortOrder.NewestFirst -> this
    ListingSortOrder.PriceAsc -> sortedBy { listing ->
        when (currency) {
            ListingCurrency.USD -> listing.priceUsd
            ListingCurrency.RUB -> listing.priceRub
        }
    }
    ListingSortOrder.PriceDesc -> sortedByDescending { listing ->
        when (currency) {
            ListingCurrency.USD -> listing.priceUsd
            ListingCurrency.RUB -> listing.priceRub
        }
    }
}
