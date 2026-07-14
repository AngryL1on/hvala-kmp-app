package tech.appard.hvala.shared.feature.listings.domain.model

data class CreateListingDraft(
    val title: String,
    val phone: String,
    val countryId: String,
    val regionId: String,
    val location: String,
    val price: Int,
    val currency: ListingCurrency,
    val availabilityLabel: String,
    val categoryId: String,
    val description: String,
    val photoUris: List<String>,
    val autoDetails: ListingAutoDetails?,
    val sellerId: String,
    val sellerName: String,
)
