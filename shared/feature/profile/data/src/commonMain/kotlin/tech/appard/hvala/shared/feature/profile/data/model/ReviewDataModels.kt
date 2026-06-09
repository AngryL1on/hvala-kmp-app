package tech.appard.hvala.shared.feature.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ReviewsFileDto(
    val reviews: List<ReviewDto> = emptyList(),
)

@Serializable
internal data class ReviewReplyDto(
    val text: String,
    val postedAt: String,
    val authorName: String,
)

@Serializable
internal data class ReviewDto(
    val id: String,
    val sellerId: String,
    val authorId: String? = null,
    val authorName: String,
    val rating: Int,
    val text: String,
    val postedAt: String,
    val listingTitle: String? = null,
    val avatarColorArgb: Long = 0xFFFFB74D,
    val photoUris: List<String> = emptyList(),
    val sellerReply: ReviewReplyDto? = null,
)
