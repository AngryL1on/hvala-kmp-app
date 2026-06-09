package tech.appard.hvala.shared.feature.listings.data.mapper

import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingAutoDetails
import tech.appard.hvala.shared.core.database.Listing_row as ListingRow

internal fun ListingRow.toDomain(isFavorite: Boolean): Listing = Listing(
    id = id,
    title = title,
    priceUsd = price_usd.toInt(),
    priceRub = price_rub.toInt(),
    location = location,
    categoryId = category_id,
    countryId = country_id,
    regionId = region_id,
    imageUrl = image_url,
    totalImages = total_images.toInt(),
    currentImage = current_image.toInt(),
    isFavorite = isFavorite,
    phone = phone,
    description = description,
    availability = availability,
    autoDetails = autoDetailsOrNull(),
    sellerId = seller_id,
    sellerName = seller_name,
    postedAt = posted_at,
)

internal fun Listing.toRow(): ListingRow = ListingRow(
    id = id,
    title = title,
    price_usd = priceUsd.toLong(),
    price_rub = priceRub.toLong(),
    location = location,
    category_id = categoryId,
    country_id = countryId,
    region_id = regionId,
    image_url = imageUrl,
    total_images = totalImages.toLong(),
    current_image = currentImage.toLong(),
    phone = phone,
    description = description,
    availability = availability,
    seller_id = sellerId,
    seller_name = sellerName,
    posted_at = postedAt,
    auto_body_type = autoDetails?.bodyType,
    auto_color = autoDetails?.color,
    auto_transmission = autoDetails?.transmission,
    auto_drivetrain = autoDetails?.drivetrain,
    auto_steering_wheel = autoDetails?.steeringWheel,
    auto_condition = autoDetails?.condition,
    auto_number_of_owners = autoDetails?.numberOfOwners,
)

internal fun HvalaDatabase.insertListing(listing: Listing) {
    val row = listing.toRow()
    listingRowQueries.insertOrReplace(
        id = row.id,
        title = row.title,
        price_usd = row.price_usd,
        price_rub = row.price_rub,
        location = row.location,
        category_id = row.category_id,
        country_id = row.country_id,
        region_id = row.region_id,
        image_url = row.image_url,
        total_images = row.total_images,
        current_image = row.current_image,
        phone = row.phone,
        description = row.description,
        availability = row.availability,
        seller_id = row.seller_id,
        seller_name = row.seller_name,
        posted_at = row.posted_at,
        auto_body_type = row.auto_body_type,
        auto_color = row.auto_color,
        auto_transmission = row.auto_transmission,
        auto_drivetrain = row.auto_drivetrain,
        auto_steering_wheel = row.auto_steering_wheel,
        auto_condition = row.auto_condition,
        auto_number_of_owners = row.auto_number_of_owners,
    )
}

private fun ListingRow.autoDetailsOrNull(): ListingAutoDetails? {
    val bodyType = auto_body_type ?: return null
    return ListingAutoDetails(
        bodyType = bodyType,
        color = auto_color.orEmpty(),
        transmission = auto_transmission.orEmpty(),
        drivetrain = auto_drivetrain.orEmpty(),
        steeringWheel = auto_steering_wheel.orEmpty(),
        condition = auto_condition.orEmpty(),
        numberOfOwners = auto_number_of_owners.orEmpty(),
    )
}

internal fun HvalaDatabase.loadAllListings(): List<Listing> {
    val favorites = favoriteRowQueries.selectAll().executeAsList().toSet()
    return listingRowQueries.selectAll().executeAsList().map { row ->
        row.toDomain(isFavorite = row.id in favorites)
    }
}

internal fun HvalaDatabase.isListingFavorite(listingId: String): Boolean =
    favoriteRowQueries.isFavorite(listingId).executeAsOne()
