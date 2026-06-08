package tech.appard.hvala.shared.feature.profile.ui.mapper

import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile
import tech.appard.hvala.shared.feature.profile.ui.model.UISellerProfile
import tech.appard.hvala.shared.feature.profile.ui.model.UIUserProfile

fun UserProfile.toUi(): UIUserProfile = UIUserProfile(
    id = id,
    fullName = fullName,
    email = email,
)

fun UIUserProfile.toDomain(): UserProfile = UserProfile(
    id = id,
    fullName = fullName,
    email = email,
)

fun SellerProfile.toUi(): UISellerProfile = UISellerProfile(
    id = id,
    name = name,
    activeListingsCount = activeListingsCount,
    rating = rating,
    memberSince = memberSince,
    avatarColorArgb = avatarColorArgb,
)

fun UISellerProfile.toDomain(): SellerProfile = SellerProfile(
    id = id,
    name = name,
    activeListingsCount = activeListingsCount,
    rating = rating,
    memberSince = memberSince,
    avatarColorArgb = avatarColorArgb,
)
