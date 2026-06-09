package tech.appard.hvala.shared.feature.profile.domain

import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewReplyError
import tech.appard.hvala.shared.feature.profile.domain.model.SubmitReviewReplyRequest
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository

class SubmitReviewReplyUseCase(
    private val authRepository: AuthRepository,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(request: SubmitReviewReplyRequest): Result<Unit> {
        if (!authRepository.isAuthenticated()) {
            return Result.failure(SubmitReviewReplyException(SubmitReviewReplyError.NotAuthenticated))
        }

        val profile = getCurrentProfileUseCase()
        if (profile.id != request.sellerId) {
            return Result.failure(SubmitReviewReplyException(SubmitReviewReplyError.NotSeller))
        }

        if (request.text.isBlank()) {
            return Result.failure(SubmitReviewReplyException(SubmitReviewReplyError.EmptyText))
        }

        reviewsRepository.ensureLoaded()
        val review = reviewsRepository.getReviewsForSeller(request.sellerId)
            .firstOrNull { it.id == request.reviewId }
            ?: return Result.failure(SubmitReviewReplyException(SubmitReviewReplyError.ReviewNotFound))

        if (review.sellerReply != null) {
            return Result.failure(SubmitReviewReplyException(SubmitReviewReplyError.AlreadyReplied))
        }

        reviewsRepository.addSellerReply(
            sellerId = request.sellerId,
            reviewId = request.reviewId,
            reply = ReviewReply(
                text = request.text.trim(),
                postedAt = currentReviewIsoDate(),
                authorName = profile.fullName,
            ),
        )
        return Result.success(Unit)
    }
}

class SubmitReviewReplyException(val error: SubmitReviewReplyError) : Exception()
