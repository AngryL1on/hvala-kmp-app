package tech.appard.hvala.shared.feature.profile.presentation.mapper

import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewSummary
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewsBundle
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReview
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReviewReply
import tech.appard.hvala.shared.feature.profile.presentation.model.UIReviewSummary

fun ReviewsBundle.toUi(canReplyToReviews: Boolean = false): ReviewsUiBundle = ReviewsUiBundle(
    sellerId = sellerId,
    sellerName = sellerName,
    summary = summary.toUi(),
    reviews = reviews.map { it.toUi(canReply = canReplyToReviews) },
)

data class ReviewsUiBundle(
    val sellerId: String,
    val sellerName: String,
    val summary: UIReviewSummary,
    val reviews: List<UIReview>,
)

fun ReviewSummary.toUi(): UIReviewSummary = UIReviewSummary(
    averageRating = averageRating,
    totalCount = totalCount,
    ratingDistribution = ratingDistribution,
)

fun Review.toUi(canReply: Boolean = false): UIReview = UIReview(
    id = id,
    authorName = authorName,
    authorInitials = authorName.initials(),
    rating = rating,
    text = text,
    postedAt = postedAt.formatAsReviewDate(),
    postedAtIso = postedAt,
    listingTitle = listingTitle,
    avatarColorArgb = avatarColorArgb,
    photoUris = photoUris,
    sellerReply = sellerReply?.toUi(),
    canReply = canReply && sellerReply == null,
)

fun ReviewReply.toUi(): UIReviewReply = UIReviewReply(
    text = text,
    postedAt = postedAt.formatAsReviewDate(),
    authorName = authorName,
)

private fun String.initials(): String {
    val parts = trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts.first().take(2).uppercase()
        else -> "${parts.first().first()}${parts.last().first()}".uppercase()
    }
}

private fun String.formatAsReviewDate(): String {
    val parts = split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else this
}
