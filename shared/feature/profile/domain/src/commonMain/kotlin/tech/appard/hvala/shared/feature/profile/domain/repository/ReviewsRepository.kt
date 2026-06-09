package tech.appard.hvala.shared.feature.profile.domain.repository

import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply

interface ReviewsRepository {
    suspend fun ensureLoaded()

    fun getReviewsForSeller(sellerId: String): List<Review>

    fun hasReviewFromAuthor(sellerId: String, authorId: String): Boolean

    suspend fun addReview(review: Review)

    suspend fun addSellerReply(sellerId: String, reviewId: String, reply: ReviewReply)
}
