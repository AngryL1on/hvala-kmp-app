package tech.appard.hvala.shared.core.contracts.model

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
