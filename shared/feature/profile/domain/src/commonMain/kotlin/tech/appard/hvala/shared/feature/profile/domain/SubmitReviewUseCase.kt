package tech.appard.hvala.shared.feature.profile.domain

import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewError
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewRequest
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository

class SubmitReviewUseCase(
    private val authRepository: AuthRepository,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(request: SubmitReviewRequest): Result<Unit> {
        if (!authRepository.isAuthenticated()) {
            return Result.failure(SubmitReviewException(SubmitReviewError.NotAuthenticated))
        }

        val profile = getCurrentProfileUseCase()
        if (request.sellerId == profile.id) {
            return Result.failure(SubmitReviewException(SubmitReviewError.SelfReview))
        }

        if (request.rating !in 1..5) {
            return Result.failure(SubmitReviewException(SubmitReviewError.InvalidRating))
        }

        if (request.text.isBlank()) {
            return Result.failure(SubmitReviewException(SubmitReviewError.EmptyText))
        }

        reviewsRepository.ensureLoaded()
        if (reviewsRepository.hasReviewFromAuthor(request.sellerId, profile.id)) {
            return Result.failure(SubmitReviewException(SubmitReviewError.AlreadyReviewed))
        }

        reviewsRepository.addReview(
            Review(
                id = "user-${profile.id}-${request.sellerId}",
                sellerId = request.sellerId,
                authorId = profile.id,
                authorName = profile.fullName,
                rating = request.rating,
                text = request.text.trim(),
                postedAt = currentReviewIsoDate(),
                listingTitle = request.listingTitle?.takeIf { it.isNotBlank() },
                photoUris = request.photoUris,
            ),
        )
        return Result.success(Unit)
    }
}

class SubmitReviewException(val error: SubmitReviewError) : Exception()
