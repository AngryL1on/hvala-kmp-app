package tech.appard.hvala.shared.feature.listings.presentation.mapper

import tech.appard.hvala.shared.core.i18n.AppLanguage
import tech.appard.hvala.shared.core.i18n.ListingSortOrderId
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingAutoDetails
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCategory
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCurrency
import tech.appard.hvala.shared.feature.listings.domain.model.ListingSortOrder
import tech.appard.hvala.shared.feature.listings.domain.model.ListingsFilters
import tech.appard.hvala.shared.feature.listings.domain.model.LocationOption
import tech.appard.hvala.shared.feature.listings.domain.model.sortedBy
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingAutoDetails
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCurrency
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingSortOrder
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.feature.listings.presentation.model.UILocationOption

fun Listing.toUi(): UIListing = UIListing(
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
    autoDetails = autoDetails?.toUi(),
    sellerId = sellerId,
    sellerName = sellerName,
    postedAt = postedAt,
)

fun UIListing.toDomain(): Listing = Listing(
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

fun ListingAutoDetails.toUi(): UIListingAutoDetails = UIListingAutoDetails(
    bodyType = bodyType,
    color = color,
    transmission = transmission,
    drivetrain = drivetrain,
    steeringWheel = steeringWheel,
    condition = condition,
    numberOfOwners = numberOfOwners,
)

fun UIListingAutoDetails.toDomain(): ListingAutoDetails = ListingAutoDetails(
    bodyType = bodyType,
    color = color,
    transmission = transmission,
    drivetrain = drivetrain,
    steeringWheel = steeringWheel,
    condition = condition,
    numberOfOwners = numberOfOwners,
)

fun ListingCategory.toUi(language: AppLanguage): UIListingCategory {
    val strings = language.strings().listings
    return UIListingCategory(
        id = id,
        title = strings.categoryTitle(id, fallback = title),
    )
}

fun UIListingCategory.toDomain(): ListingCategory = ListingCategory(id = id, title = title)

fun LocationOption.toUi(language: AppLanguage, countryId: String? = null): UILocationOption {
    val strings = language.strings().listings
    val localizedTitle = when {
        countryId != null -> strings.regionTitle(id, fallback = title)
        id in listOf("ru", "rs") -> strings.countryTitle(id, fallback = title)
        else -> title
    }
    return UILocationOption(id = id, title = localizedTitle)
}

fun UILocationOption.toDomain(): LocationOption = LocationOption(id = id, title = title)

fun ListingsFilters.toUi(): UIListingsFilters = UIListingsFilters(
    currency = currency.toUi(),
    minPrice = minPrice,
    maxPrice = maxPrice,
    countryId = countryId,
    regionId = regionId,
    categoryId = categoryId,
)

fun UIListingsFilters.toDomain(): ListingsFilters = ListingsFilters(
    currency = currency.toDomain(),
    minPrice = minPrice,
    maxPrice = maxPrice,
    countryId = countryId,
    regionId = regionId,
    categoryId = categoryId,
)

fun ListingCurrency.toUi(): UIListingCurrency = when (this) {
    ListingCurrency.USD -> UIListingCurrency.USD
    ListingCurrency.RUB -> UIListingCurrency.RUB
}

fun UIListingCurrency.toDomain(): ListingCurrency = when (this) {
    UIListingCurrency.USD -> ListingCurrency.USD
    UIListingCurrency.RUB -> ListingCurrency.RUB
}

fun ListingSortOrder.toUi(): UIListingSortOrder = when (this) {
    ListingSortOrder.NewestFirst -> UIListingSortOrder.NewestFirst
    ListingSortOrder.PriceAsc -> UIListingSortOrder.PriceAsc
    ListingSortOrder.PriceDesc -> UIListingSortOrder.PriceDesc
}

fun UIListingSortOrder.toDomain(): ListingSortOrder = when (this) {
    UIListingSortOrder.NewestFirst -> ListingSortOrder.NewestFirst
    UIListingSortOrder.PriceAsc -> ListingSortOrder.PriceAsc
    UIListingSortOrder.PriceDesc -> ListingSortOrder.PriceDesc
}

fun List<Listing>.toListingsUi(): List<UIListing> = map { it.toUi() }

fun List<ListingCategory>.toCategoriesUi(language: AppLanguage): List<UIListingCategory> =
    map { it.toUi(language) }

fun List<LocationOption>.toLocationsUi(language: AppLanguage, countryId: String? = null): List<UILocationOption> =
    map { it.toUi(language, countryId) }

fun Map<String, List<LocationOption>>.toRegionsByCountryUi(language: AppLanguage): Map<String, List<UILocationOption>> =
    mapValues { (countryId, regions) -> regions.toLocationsUi(language, countryId) }

fun UIListingSortOrder.toSortOrderId(): ListingSortOrderId = when (this) {
    UIListingSortOrder.NewestFirst -> ListingSortOrderId.NewestFirst
    UIListingSortOrder.PriceAsc -> ListingSortOrderId.PriceAsc
    UIListingSortOrder.PriceDesc -> ListingSortOrderId.PriceDesc
}

fun List<UIListing>.sortedByUi(
    sortOrder: UIListingSortOrder,
    currency: UIListingCurrency,
): List<UIListing> = map { it.toDomain() }
    .sortedBy(sortOrder.toDomain(), currency.toDomain())
    .toListingsUi()
