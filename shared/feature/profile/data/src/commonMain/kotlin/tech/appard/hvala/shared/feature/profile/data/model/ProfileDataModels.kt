package tech.appard.hvala.shared.feature.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SellersFileDto(
    val sellers: List<SellerProfileDto>,
)

@Serializable
internal data class SellerProfileDto(
    val id: String,
    val name: String,
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val avatarColorArgb: Long = 0xFFFFB74D,
)

@Serializable
internal data class ProfileOverviewDto(
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val activeListingIds: List<String>,
    val archiveListingIds: List<String>,
)
