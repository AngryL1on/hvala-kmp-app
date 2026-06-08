package tech.appard.hvala.shared.feature.profile.domain.model

data class ProfileOverview(
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val activeListingIds: List<String>,
    val archiveListingIds: List<String>,
)

data class SellerProfile(
    val id: String,
    val name: String,
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val avatarColorArgb: Long = 0xFFFFB74D,
)
