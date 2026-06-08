package tech.appard.hvala.shared.feature.listings.domain.model

data class Listing(
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
    val autoDetails: ListingAutoDetails? = null,
    val sellerId: String = "",
    val sellerName: String = "",
    val postedAt: String = "",
)

data class ListingCategory(
    val id: String,
    val title: String,
)
