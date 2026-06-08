package tech.appard.hvala.shared.feature.profile.data.mapper

import tech.appard.hvala.shared.feature.profile.data.model.ProfileOverviewDto
import tech.appard.hvala.shared.feature.profile.data.model.SellerProfileDto
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile

internal fun ProfileOverviewDto.toDomain(): ProfileOverview = ProfileOverview(
    activeListingsCount = activeListingsCount,
    rating = rating,
    memberSince = memberSince,
    activeListingIds = activeListingIds,
    archiveListingIds = archiveListingIds,
)

internal fun SellerProfileDto.toDomain(): SellerProfile = SellerProfile(
    id = id,
    name = name,
    activeListingsCount = activeListingsCount,
    rating = rating,
    memberSince = memberSince,
    avatarColorArgb = avatarColorArgb,
)
