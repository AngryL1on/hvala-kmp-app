package tech.appard.hvala.shared.core.contracts.model

data class Listing(
    val id: String,
    val title: String,
    val priceUsd: Int,
    val priceRub: Int,
    val location: String,
    val imageUrl: String? = null,
    val totalImages: Int = 4,
    val currentImage: Int = 1,
    val isFavorite: Boolean = false,
)

data class ListingCategory(
    val id: String,
    val title: String,
)
