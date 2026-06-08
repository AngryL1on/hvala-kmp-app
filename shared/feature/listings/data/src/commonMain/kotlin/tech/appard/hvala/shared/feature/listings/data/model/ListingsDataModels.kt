package tech.appard.hvala.shared.feature.listings.data.model

import kotlinx.serialization.Serializable
import tech.appard.hvala.shared.feature.listings.data.mapper.toDomain
import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.model.ListingCategory
import tech.appard.hvala.shared.feature.listings.domain.model.LocationOption

@Serializable
internal data class ListingsFileDto(
    val listings: List<ListingDto>,
)

@Serializable
internal data class ListingDto(
    val id: String,
    val title: String,
    val priceUsd: Int,
    val priceRub: Int,
    val location: String,
    val categoryId: String = "clothes",
    val countryId: String = "ru",
    val regionId: String = "moscow",
    val imageUrl: String? = null,
    val totalImages: Int = 4,
    val currentImage: Int = 1,
    val isFavorite: Boolean = false,
    val phone: String = "",
    val description: String = "",
    val availability: String = "Available",
    val autoDetails: ListingAutoDetailsDto? = null,
    val sellerId: String = "",
    val sellerName: String = "",
    val postedAt: String = "",
)

@Serializable
internal data class ListingAutoDetailsDto(
    val bodyType: String,
    val color: String,
    val transmission: String,
    val drivetrain: String,
    val steeringWheel: String,
    val condition: String,
    val numberOfOwners: String,
)

@Serializable
internal data class FilterDefaultsFileDto(
    val categories: List<ListingCategoryDto>,
    val countries: List<LocationOptionDto>,
    val regionsByCountry: Map<String, List<LocationOptionDto>>,
)

@Serializable
internal data class ListingCategoryDto(
    val id: String,
    val title: String,
)

@Serializable
internal data class LocationOptionDto(
    val id: String,
    val title: String,
)

internal fun ListingsFileDto.toDomainListings(): List<Listing> =
    listings.map { it.toDomain() }

internal fun FilterDefaultsFileDto.toDomainCategories(): List<ListingCategory> =
    categories.map { it.toDomain() }

internal fun FilterDefaultsFileDto.toDomainCountries(): List<LocationOption> =
    countries.map { it.toDomain() }

internal fun FilterDefaultsFileDto.toDomainRegionsByCountry(): Map<String, List<LocationOption>> =
    regionsByCountry.mapValues { (_, regions) -> regions.map { it.toDomain() } }
