package tech.appard.hvala.shared.feature.profile

enum class ProfileListingsTab {
    Active,
    Archive,
}

data class ProfileListing(
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
