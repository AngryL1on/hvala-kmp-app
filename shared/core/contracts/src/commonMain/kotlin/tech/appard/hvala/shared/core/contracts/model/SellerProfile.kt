package tech.appard.hvala.shared.core.contracts.model

data class SellerProfile(
    val id: String,
    val name: String,
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val avatarColorArgb: Long = 0xFFFFB74D,
)
