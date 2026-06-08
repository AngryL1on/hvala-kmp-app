package tech.appard.hvala.shared.feature.listings.data.mapper

import tech.appard.hvala.shared.feature.listings.data.model.ListingAutoDetailsDto
import tech.appard.hvala.shared.feature.listings.data.model.ListingCategoryDto
import tech.appard.hvala.shared.feature.listings.data.model.ListingDto
import tech.appard.hvala.shared.feature.listings.data.model.LocationOptionDto
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingAutoDetails
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCategory
import tech.appard.hvala.shared.feature.listings.domain.model.LocationOption

internal fun ListingDto.toDomain(): Listing = Listing(
    id = id,
    title = title,
    priceUsd = priceUsd,
    priceRub = priceRub,
    location = location,
    categoryId = categoryId,
    countryId = countryId,
    regionId = regionId,
    imageUrl = imageUrl,
    totalImages = totalImages,
    currentImage = currentImage,
    isFavorite = isFavorite,
    phone = phone,
    description = description,
    availability = availability,
    autoDetails = autoDetails?.toDomain(),
    sellerId = sellerId,
    sellerName = sellerName,
    postedAt = postedAt,
)

internal fun ListingAutoDetailsDto.toDomain(): ListingAutoDetails = ListingAutoDetails(
    bodyType = bodyType,
    color = color,
    transmission = transmission,
    drivetrain = drivetrain,
    steeringWheel = steeringWheel,
    condition = condition,
    numberOfOwners = numberOfOwners,
)

internal fun ListingCategoryDto.toDomain(): ListingCategory = ListingCategory(
    id = id,
    title = title,
)

internal fun LocationOptionDto.toDomain(): LocationOption = LocationOption(
    id = id,
    title = title,
)
