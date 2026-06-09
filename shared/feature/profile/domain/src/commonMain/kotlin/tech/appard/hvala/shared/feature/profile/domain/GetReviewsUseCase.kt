package tech.appard.hvala.shared.feature.profile.domain

import tech.appard.hvala.shared.feature.auth.domain.GetCurrentProfileUseCase
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewsBundle
import tech.appard.hvala.shared.feature.profile.domain.model.toSummary
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

class GetReviewsUseCase(
    private val reviewsRepository: ReviewsRepository,
    private val sellerRepository: SellerRepository,
    private val getCurrentProfileUseCase: GetCurrentProfileUseCase,
) {
    suspend operator fun invoke(sellerId: String): ReviewsBundle {
        reviewsRepository.ensureLoaded()
        sellerRepository.ensureLoaded()

        val reviews = reviewsRepository.getReviewsForSeller(sellerId)
        val currentProfile = getCurrentProfileUseCase()
        val sellerName = sellerRepository.getSellerById(sellerId)?.name
            ?: if (sellerId == currentProfile.id) currentProfile.fullName
            else ""

        return ReviewsBundle(
            sellerId = sellerId,
            sellerName = sellerName,
            summary = reviews.toSummary(),
            reviews = reviews.sortedByDescending { it.postedAt },
        )
    }
}
