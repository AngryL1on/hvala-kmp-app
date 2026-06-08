package tech.appard.hvala.shared.core.contracts.model

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

object ListingFilterDefaults {
    fun categories(): List<ListingCategory> = listOf(
        ListingCategory(id = "clothes", title = "Одежда"),
        ListingCategory(id = "auto", title = "Авто"),
        ListingCategory(id = "realty", title = "Недвижимость"),
        ListingCategory(id = "electronics", title = "Электроника"),
        ListingCategory(id = "furniture", title = "Мебель"),
        ListingCategory(id = "jobs", title = "Работа"),
        ListingCategory(id = "services", title = "Услуги"),
        ListingCategory(id = "hobby", title = "Хобби"),
        ListingCategory(id = "kids", title = "Детям"),
        ListingCategory(id = "pets", title = "Животные"),
    )

    fun countries(): List<LocationOption> = listOf(
        LocationOption(id = "ru", title = "Россия"),
        LocationOption(id = "rs", title = "Сербия"),
    )

    fun regionsByCountry(): Map<String, List<LocationOption>> = mapOf(
        "ru" to listOf(
            LocationOption(id = "moscow", title = "Москва"),
            LocationOption(id = "moscow_region", title = "Московская область"),
            LocationOption(id = "spb", title = "Санкт-Петербург"),
        ),
        "rs" to listOf(
            LocationOption(id = "belgrade", title = "Belgrade"),
            LocationOption(id = "sumadija", title = "Šumadija and Western Serbia"),
        ),
    )
}
