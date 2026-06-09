package tech.appard.hvala.shared.feature.profile.data.repository

import tech.appard.hvala.shared.core.database.DatabaseSeedKeys
import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.core.database.isSeeded
import tech.appard.hvala.shared.core.database.markSeeded
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.profile.data.mapper.insertProfileOverview
import tech.appard.hvala.shared.feature.profile.data.mapper.insertReview
import tech.appard.hvala.shared.feature.profile.data.mapper.insertSeller
import tech.appard.hvala.shared.feature.profile.data.mapper.loadReviewsForSeller
import tech.appard.hvala.shared.feature.profile.data.mapper.toDomain
import tech.appard.hvala.shared.feature.profile.data.mapper.toProfileOverview
import tech.appard.hvala.shared.feature.profile.data.mapper.toSellerProfile
import tech.appard.hvala.shared.feature.profile.data.mapper.updateReviewReply
import tech.appard.hvala.shared.feature.profile.data.source.ProfileJsonDataSource
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile
import tech.appard.hvala.shared.feature.profile.domain.repository.ProfileOverviewRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.ReviewsRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

internal class JsonProfileOverviewRepository(
    private val database: HvalaDatabase,
    private val dataSource: ProfileJsonDataSource,
) : ProfileOverviewRepository {
    private var profileOverview: ProfileOverview? = null

    override suspend fun ensureLoaded() {
        if (profileOverview != null) return

        if (database.isSeeded(DatabaseSeedKeys.PROFILE_OVERVIEW)) {
            profileOverview = database.profileOverviewRowQueries.selectOne()
                .executeAsOneOrNull()
                ?.toProfileOverview()
            return
        }

        val overview = dataSource.profileOverview().toDomain()
        database.transaction {
            database.insertProfileOverview(overview)
            database.markSeeded(DatabaseSeedKeys.PROFILE_OVERVIEW)
        }
        profileOverview = overview
    }

    override fun getProfileOverview(): ProfileOverview =
        profileOverview ?: ProfileOverview(
            activeListingsCount = 0,
            rating = 0f,
            memberSince = "",
            activeListingIds = emptyList(),
            archiveListingIds = emptyList(),
        )
}

internal class JsonSellerRepository(
    private val database: HvalaDatabase,
    private val dataSource: ProfileJsonDataSource,
    private val listingsRepository: ListingsRepository,
) : SellerRepository {
    private var sellers: Map<String, SellerProfile> = emptyMap()

    override suspend fun ensureLoaded() {
        if (sellers.isNotEmpty()) return
        listingsRepository.ensureLoaded()

        if (database.isSeeded(DatabaseSeedKeys.SELLERS)) {
            sellers = database.sellerRowQueries.selectAll()
                .executeAsList()
                .associate { row -> row.id to row.toSellerProfile() }
            return
        }

        val loaded = dataSource.sellers().sellers.associate { seller ->
            seller.id to seller.toDomain()
        }
        database.transaction {
            loaded.values.forEach { database.insertSeller(it) }
            database.markSeeded(DatabaseSeedKeys.SELLERS)
        }
        sellers = loaded
    }

    override fun getSellerById(id: String): SellerProfile? = sellers[id]

    override fun getListingsForSeller(sellerId: String): List<Listing> =
        listingsRepository.listings.value
            .filter { it.sellerId == sellerId }
}

internal class JsonReviewsRepository(
    private val database: HvalaDatabase,
    private val dataSource: ProfileJsonDataSource,
) : ReviewsRepository {
    override suspend fun ensureLoaded() {
        if (database.isSeeded(DatabaseSeedKeys.REVIEWS)) return

        val reviews = dataSource.reviews().reviews.map { it.toDomain() }
        database.transaction {
            reviews.forEach { database.insertReview(it) }
            database.markSeeded(DatabaseSeedKeys.REVIEWS)
        }
    }

    override fun getReviewsForSeller(sellerId: String): List<Review> =
        database.loadReviewsForSeller(sellerId)

    override fun hasReviewFromAuthor(sellerId: String, authorId: String): Boolean =
        database.loadReviewsForSeller(sellerId).any { it.authorId == authorId }

    override suspend fun addReview(review: Review) {
        ensureLoaded()
        database.insertReview(review)
    }

    override suspend fun addSellerReply(sellerId: String, reviewId: String, reply: ReviewReply) {
        ensureLoaded()
        database.updateReviewReply(reviewId, reply)
    }
}
