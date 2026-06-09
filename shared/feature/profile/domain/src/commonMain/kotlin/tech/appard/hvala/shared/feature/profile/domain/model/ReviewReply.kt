package tech.appard.hvala.shared.feature.profile.domain.model

data class ReviewReply(
    val text: String,
    val postedAt: String,
    val authorName: String,
)

data class SubmitReviewReplyRequest(
    val sellerId: String,
    val reviewId: String,
    val text: String,
)

sealed interface SubmitReviewReplyError {
    data object NotAuthenticated : SubmitReviewReplyError
    data object NotSeller : SubmitReviewReplyError
    data object AlreadyReplied : SubmitReviewReplyError
    data object EmptyText : SubmitReviewReplyError
    data object ReviewNotFound : SubmitReviewReplyError
}
