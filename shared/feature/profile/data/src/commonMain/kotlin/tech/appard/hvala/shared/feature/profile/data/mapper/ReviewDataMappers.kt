package tech.appard.hvala.shared.feature.profile.data.mapper

import tech.appard.hvala.shared.feature.profile.data.model.ReviewDto
import tech.appard.hvala.shared.feature.profile.data.model.ReviewReplyDto
import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply

internal fun ReviewDto.toDomain(): Review = Review(
    id = id,
    sellerId = sellerId,
    authorId = authorId,
    authorName = authorName,
    rating = rating.coerceIn(1, 5),
    text = text,
    postedAt = postedAt,
    listingTitle = listingTitle,
    avatarColorArgb = avatarColorArgb,
    photoUris = photoUris,
    sellerReply = sellerReply?.toDomain(),
)

internal fun ReviewReplyDto.toDomain(): ReviewReply = ReviewReply(
    text = text,
    postedAt = postedAt,
    authorName = authorName,
)
