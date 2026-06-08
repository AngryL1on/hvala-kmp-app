package tech.appard.hvala.shared.core.contracts.model

object SellerMockCatalog {
    private const val DEFAULT_SELLER_ID = "alex"

    fun sellerById(id: String): SellerProfile? = sellers[id]

    fun defaultSellerId(): String = DEFAULT_SELLER_ID

    fun listingsForSeller(sellerId: String): List<Listing> =
        ListingMockCatalog.allListings()
            .filter { it.sellerId == sellerId }
            .map(ListingMockCatalog::withDetailFields)

    fun resolveSellerName(sellerId: String): String =
        sellerById(sellerId)?.name ?: "Alex M."

    fun resolveSellerForListing(listing: Listing): Listing {
        val sellerId = listing.sellerId.ifBlank { DEFAULT_SELLER_ID }
        return listing.copy(
            sellerId = sellerId,
            sellerName = listing.sellerName.ifBlank { resolveSellerName(sellerId) },
        )
    }

    private val sellers: Map<String, SellerProfile> = mapOf(
        "alex" to SellerProfile(
            id = "alex",
            name = "Alex M.",
            activeListingsCount = 12,
            rating = 4.6f,
            memberSince = "On Hvala since June 2024",
            avatarColorArgb = 0xFF5C9FD6,
        ),
        "niko" to SellerProfile(
            id = "niko",
            name = "Niko B.",
            activeListingsCount = 8,
            rating = 4.8f,
            memberSince = "On Hvala since March 2024",
            avatarColorArgb = 0xFFFFB74D,
        ),
        "lincoln" to SellerProfile(
            id = "lincoln",
            name = "Abraham Lincoln",
            activeListingsCount = 5,
            rating = 4.3f,
            memberSince = "On Hvala since January 2024",
            avatarColorArgb = 0xFF03989F,
        ),
        "yeltsin" to SellerProfile(
            id = "yeltsin",
            name = "Boris Yeltsin",
            activeListingsCount = 6,
            rating = 4.1f,
            memberSince = "On Hvala since August 2023",
            avatarColorArgb = 0xFF5C9FD6,
        ),
        "pushkin" to SellerProfile(
            id = "pushkin",
            name = "Alexander Pushkin",
            activeListingsCount = 3,
            rating = 4.9f,
            memberSince = "On Hvala since May 2024",
            avatarColorArgb = 0xFFFFBF34,
        ),
        "gagarin" to SellerProfile(
            id = "gagarin",
            name = "Yuri Gagarin",
            activeListingsCount = 4,
            rating = 4.7f,
            memberSince = "On Hvala since February 2024",
            avatarColorArgb = 0xFFFF8A65,
        ),
        "tereshkova" to SellerProfile(
            id = "tereshkova",
            name = "Valentina Tereshkova",
            activeListingsCount = 7,
            rating = 4.5f,
            memberSince = "On Hvala since September 2023",
            avatarColorArgb = 0xFFE57373,
        ),
    )
}
