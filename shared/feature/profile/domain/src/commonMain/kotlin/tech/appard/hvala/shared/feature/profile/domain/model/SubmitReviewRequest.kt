package tech.appard.hvala.shared.feature.profile.domain.model

data class SubmitReviewRequest(
    val sellerId: String,
    val rating: Int,
    val text: String,
    val listingTitle: String? = null,
    val photoUris: List<String> = emptyList(),
)

sealed interface SubmitReviewError {
    data object NotAuthenticated : SubmitReviewError
    data object SelfReview : SubmitReviewError
    data object AlreadyReviewed : SubmitReviewError
    data object InvalidRating : SubmitReviewError
    data object EmptyText : SubmitReviewError
}
