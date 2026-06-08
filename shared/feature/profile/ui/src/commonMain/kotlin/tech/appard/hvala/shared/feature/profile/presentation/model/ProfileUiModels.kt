package tech.appard.hvala.shared.feature.profile.presentation.model

data class UIUserProfile(
    val id: String,
    val fullName: String,
    val email: String,
)

data class UISellerProfile(
    val id: String,
    val name: String,
    val activeListingsCount: Int,
    val rating: Float,
    val memberSince: String,
    val avatarColorArgb: Long = 0xFFFFB74D,
)

enum class ProfileListingsTab {
    Active,
    Archive,
}

val ProfileListingsTab.pageIndex: Int
    get() = ordinal

fun profileListingsTab(pageIndex: Int): ProfileListingsTab =
    ProfileListingsTab.entries[pageIndex.coerceIn(ProfileListingsTab.entries.indices)]
