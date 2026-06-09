package tech.appard.hvala.shared.feature.profile.presentation.model

enum class ReviewSortOrder {
    NewestFirst,
    OldestFirst,
    PositiveFirst,
    NegativeFirst,
}

data class UIReview(
    val id: String,
    val authorName: String,
    val authorInitials: String,
    val rating: Int,
    val text: String,
    val postedAt: String,
    val postedAtIso: String,
    val listingTitle: String?,
    val avatarColorArgb: Long,
    val photoUris: List<String> = emptyList(),
    val sellerReply: UIReviewReply? = null,
    val canReply: Boolean = false,
)

data class UIReviewReply(
    val text: String,
    val postedAt: String,
    val authorName: String,
)

data class UIReviewSummary(
    val averageRating: Float,
    val totalCount: Int,
    val ratingDistribution: Map<Int, Int>,
)

data class ReviewListingOption(
    val id: String,
    val label: String,
)
