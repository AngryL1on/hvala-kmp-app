package tech.appard.hvala.shared.feature.profile.data.repository

import tech.appard.hvala.shared.feature.profile.data.mapper.toDomain
import tech.appard.hvala.shared.feature.profile.data.source.ProfileJsonDataSource
import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository

internal class JsonReviewsRepository(
    private val dataSource: ProfileJsonDataSource,
) : ReviewsRepository {
    private val reviewsBySeller = mutableMapOf<String, MutableList<Review>>()

    override suspend fun ensureLoaded() {
        if (reviewsBySeller.isNotEmpty()) return
        dataSource.reviews().reviews
            .map { it.toDomain() }
            .groupBy { it.sellerId }
            .forEach { (sellerId, reviews) ->
                reviewsBySeller[sellerId] = reviews.toMutableList()
            }
    }

    override fun getReviewsForSeller(sellerId: String): List<Review> =
        reviewsBySeller[sellerId].orEmpty()

    override fun hasReviewFromAuthor(sellerId: String, authorId: String): Boolean =
        reviewsBySeller[sellerId].orEmpty().any { it.authorId == authorId }

    override suspend fun addReview(review: Review) {
        ensureLoaded()
        val reviews = reviewsBySeller.getOrPut(review.sellerId) { mutableListOf() }
        reviews.removeAll { it.id == review.id }
        reviews.add(review)
    }

    override suspend fun addSellerReply(sellerId: String, reviewId: String, reply: ReviewReply) {
        ensureLoaded()
        val reviews = reviewsBySeller[sellerId] ?: return
        val index = reviews.indexOfFirst { it.id == reviewId }
        if (index == -1) return
        reviews[index] = reviews[index].copy(sellerReply = reply)
    }
}
