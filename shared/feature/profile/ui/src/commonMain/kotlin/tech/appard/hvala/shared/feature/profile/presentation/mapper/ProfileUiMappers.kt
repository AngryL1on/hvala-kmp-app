package tech.appard.hvala.shared.feature.profile.presentation.mapper

import tech.appard.hvala.shared.feature.auth.domain.model.UserProfile
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile
import tech.appard.hvala.shared.feature.profile.presentation.model.UISellerProfile
import tech.appard.hvala.shared.feature.profile.presentation.model.UIUserProfile

fun UserProfile.toUi(): UIUserProfile = UIUserProfile(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
)

fun UIUserProfile.toDomain(): UserProfile = UserProfile(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
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
