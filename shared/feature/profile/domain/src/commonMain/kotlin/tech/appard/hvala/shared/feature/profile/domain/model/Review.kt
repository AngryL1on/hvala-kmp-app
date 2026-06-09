package tech.appard.hvala.shared.feature.profile.domain.model

data class Review(
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
    val sellerReply: ReviewReply? = null,
)

data class ReviewSummary(
    val averageRating: Float,
    val totalCount: Int,
    val ratingDistribution: Map<Int, Int>,
)

data class ReviewsBundle(
    val sellerId: String,
    val sellerName: String,
    val summary: ReviewSummary,
    val reviews: List<Review>,
)

fun List<Review>.toSummary(): ReviewSummary {
    if (isEmpty()) {
        return ReviewSummary(
            averageRating = 0f,
            totalCount = 0,
            ratingDistribution = emptyMap(),
        )
    }
    val distribution = (1..5).associateWith { star -> count { it.rating == star } }
    val average = sumOf { it.rating }.toFloat() / size
    return ReviewSummary(
        averageRating = average,
        totalCount = size,
        ratingDistribution = distribution,
    )
}
