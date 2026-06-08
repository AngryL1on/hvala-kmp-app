package tech.appard.hvala.shared.feature.profile

enum class ProfileListingsTab {
    Active,
    Archive,
}

val ProfileListingsTab.pageIndex: Int
    get() = ordinal

fun profileListingsTab(pageIndex: Int): ProfileListingsTab =
    ProfileListingsTab.entries[pageIndex.coerceIn(ProfileListingsTab.entries.indices)]
