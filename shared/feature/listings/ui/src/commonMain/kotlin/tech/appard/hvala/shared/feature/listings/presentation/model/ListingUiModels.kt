package tech.appard.hvala.shared.feature.listings.presentation.model

data class UIListing(
    val id: String,
    val title: String,
    val priceUsd: Int,
    val priceRub: Int,
    val location: String,
    val categoryId: String = "clothes",
    val countryId: String = "ru",
    val regionId: String = "moscow",
    val imageUrl: String? = null,
    val totalImages: Int = 4,
    val currentImage: Int = 1,
    val isFavorite: Boolean = false,
    val phone: String = "",
    val description: String = "",
    val availability: String = "Available",
    val autoDetails: UIListingAutoDetails? = null,
    val sellerId: String = "",
    val sellerName: String = "",
    val postedAt: String = "",
)

data class UIListingAutoDetails(
    val bodyType: String,
    val color: String,
    val transmission: String,
    val drivetrain: String,
    val steeringWheel: String,
    val condition: String,
    val numberOfOwners: String,
)

data class UIListingCategory(
    val id: String,
    val title: String,
)

data class UILocationOption(
    val id: String,
    val title: String,
)

enum class UIListingCurrency(val code: String, val symbol: String) {
    USD(code = "USD", symbol = "$"),
    RUB(code = "RUB", symbol = "₽"),
}

data class UIListingsFilters(
    val currency: UIListingCurrency = UIListingCurrency.USD,
    val minPrice: String = "",
    val maxPrice: String = "",
    val countryId: String? = null,
    val regionId: String? = null,
    val categoryId: String? = null,
)

enum class UIListingSortOrder(val title: String) {
    NewestFirst("Сначала новые"),
    PriceAsc("Цена ↑"),
    PriceDesc("Цена ↓"),
}
