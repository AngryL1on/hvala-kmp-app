package tech.appard.hvala.shared.feature.profile.data.mapper

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.core.database.Profile_overview_row
import tech.appard.hvala.shared.core.database.Review_row
import tech.appard.hvala.shared.core.database.Seller_row
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.Review
import tech.appard.hvala.shared.feature.profile.domain.model.ReviewReply
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile

private val listingIdsJson = Json
private val stringListSerializer = ListSerializer(String.serializer())

internal fun Seller_row.toSellerProfile(): SellerProfile = SellerProfile(
    id = id,
    name = name,
    activeListingsCount = active_listings_count.toInt(),
    rating = rating.toFloat(),
    memberSince = member_since,
    avatarColorArgb = avatar_color_argb,
)

internal fun SellerProfile.toRow(): Seller_row = Seller_row(
    id = id,
    name = name,
    active_listings_count = activeListingsCount.toLong(),
    rating = rating.toDouble(),
    member_since = memberSince,
    avatar_color_argb = avatarColorArgb,
)

internal fun ProfileOverview.toRow(): Profile_overview_row = Profile_overview_row(
    id = 1L,
    active_listings_count = activeListingsCount.toLong(),
    rating = rating.toDouble(),
    member_since = memberSince,
    active_listing_ids_json = listingIdsJson.encodeToString(stringListSerializer, activeListingIds),
    archive_listing_ids_json = listingIdsJson.encodeToString(stringListSerializer, archiveListingIds),
)

internal fun Profile_overview_row.toProfileOverview(): ProfileOverview = ProfileOverview(
    activeListingsCount = active_listings_count.toInt(),
    rating = rating.toFloat(),
    memberSince = member_since,
    activeListingIds = listingIdsJson.decodeFromString(stringListSerializer, active_listing_ids_json),
    archiveListingIds = listingIdsJson.decodeFromString(stringListSerializer, archive_listing_ids_json),
)

internal fun Review_row.toReview(photoUris: List<String>): Review = Review(
    id = id,
    sellerId = seller_id,
    authorId = author_id,
    authorName = author_name,
    rating = rating.toInt(),
    text = text,
    postedAt = posted_at,
    listingTitle = listing_title,
    avatarColorArgb = avatar_color_argb,
    photoUris = photoUris,
    sellerReply = replyOrNull(),
)

internal fun Review.toRow(): Review_row = Review_row(
    id = id,
    seller_id = sellerId,
    author_id = authorId,
    author_name = authorName,
    rating = rating.toLong(),
    text = text,
    posted_at = postedAt,
    listing_title = listingTitle,
    avatar_color_argb = avatarColorArgb,
    reply_text = sellerReply?.text,
    reply_posted_at = sellerReply?.postedAt,
    reply_author_name = sellerReply?.authorName,
)

internal fun HvalaDatabase.insertSeller(seller: SellerProfile) {
    val row = seller.toRow()
    sellerRowQueries.insertOrReplace(
        id = row.id,
        name = row.name,
        active_listings_count = row.active_listings_count,
        rating = row.rating,
        member_since = row.member_since,
        avatar_color_argb = row.avatar_color_argb,
    )
}

internal fun HvalaDatabase.insertProfileOverview(overview: ProfileOverview) {
    val row = overview.toRow()
    profileOverviewRowQueries.insertOrReplace(
        active_listings_count = row.active_listings_count,
        rating = row.rating,
        member_since = row.member_since,
        active_listing_ids_json = row.active_listing_ids_json,
        archive_listing_ids_json = row.archive_listing_ids_json,
    )
}

internal fun HvalaDatabase.loadReview(reviewId: String): Review? {
    val row = reviewRowQueries.selectById(reviewId).executeAsOneOrNull() ?: return null
    val photos = reviewPhotoRowQueries.selectForReview(reviewId).executeAsList()
    return row.toReview(photoUris = photos)
}

internal fun HvalaDatabase.insertReview(review: Review) {
    val row = review.toRow()
    reviewRowQueries.insertOrReplace(
        id = row.id,
        seller_id = row.seller_id,
        author_id = row.author_id,
        author_name = row.author_name,
        rating = row.rating,
        text = row.text,
        posted_at = row.posted_at,
        listing_title = row.listing_title,
        avatar_color_argb = row.avatar_color_argb,
        reply_text = row.reply_text,
        reply_posted_at = row.reply_posted_at,
        reply_author_name = row.reply_author_name,
    )
    reviewPhotoRowQueries.deleteForReview(review.id)
    review.photoUris.forEachIndexed { index, uri ->
        reviewPhotoRowQueries.insert(
            review_id = review.id,
            sort_order = index.toLong(),
            uri = uri,
        )
    }
}

internal fun HvalaDatabase.updateReviewReply(reviewId: String, reply: ReviewReply) {
    reviewRowQueries.updateReply(
        reply_text = reply.text,
        reply_posted_at = reply.postedAt,
        reply_author_name = reply.authorName,
        id = reviewId,
    )
}

internal fun HvalaDatabase.loadReviewsForSeller(sellerId: String): List<Review> =
    reviewRowQueries.selectForSeller(sellerId).executeAsList().map { row ->
        val photos = reviewPhotoRowQueries.selectForReview(row.id).executeAsList()
        row.toReview(photoUris = photos)
    }

private fun Review_row.replyOrNull(): ReviewReply? {
    val replyText = reply_text ?: return null
    return ReviewReply(
        text = replyText,
        postedAt = reply_posted_at.orEmpty(),
        authorName = reply_author_name.orEmpty(),
    )
}
