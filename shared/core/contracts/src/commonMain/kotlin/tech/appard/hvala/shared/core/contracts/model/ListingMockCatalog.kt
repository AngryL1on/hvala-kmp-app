package tech.appard.hvala.shared.core.contracts.model

object ListingMockCatalog {
    fun allListings(): List<Listing> = catalogListings

    fun listingById(id: String): Listing? {
        return catalogListings.find { it.id == id }?.let(::withDetailFields)
    }

    fun withDetailFields(listing: Listing): Listing {
        val sellerResolved = SellerMockCatalog.resolveSellerForListing(listing)
        return sellerResolved.copy(
        phone = sellerResolved.phone.ifBlank { "+382 67 123 456" },
        description = sellerResolved.description.ifBlank {
            "Well-maintained item in excellent condition. " +
                "Available for viewing by appointment. " +
                "Price is negotiable for serious buyers."
        },
        availability = sellerResolved.availability.ifBlank { "Available" },
        postedAt = sellerResolved.postedAt.ifBlank { "2 days ago" },
        autoDetails = sellerResolved.autoDetails ?: autoDetailsFor(sellerResolved.categoryId),
    )
    }

    private fun autoDetailsFor(categoryId: String): ListingAutoDetails? {
        if (categoryId != "auto") return null
        return ListingAutoDetails(
            bodyType = "SUV",
            color = "Black",
            transmission = "Automatic",
            drivetrain = "AWD",
            steeringWheel = "Left",
            condition = "Used",
            numberOfOwners = "2",
        )
    }

    private val catalogListings: List<Listing> = buildCatalog()

    private fun buildCatalog(): List<Listing> {
        val categories = listOf("clothes", "auto", "electronics", "realty", "furniture")
        val ruLocations = listOf(
            Triple("ru", "moscow", "Moscow"),
            Triple("ru", "moscow_region", "Khimki, Moscow Region"),
            Triple("ru", "spb", "Saint Petersburg"),
        )
        val rsLocations = listOf(
            Triple("rs", "belgrade", "Belgrade"),
            Triple("rs", "sumadija", "Šumadija and Western Serbia"),
        )
        val locations = ruLocations + rsLocations
        val titles = listOf(
            "Number Nine Hoodie",
            "Nike Air Max Sneakers",
            "Burberry Coat",
            "Louis Vuitton Bag",
            "Rolex Submariner Watch",
            "Stone Island Jacket",
            "AirPods Pro",
            "Tiffany Ring",
            "BMW X5",
            "Corner Sofa",
        )

        val sellerIds = listOf("alex", "niko", "lincoln", "yeltsin", "pushkin", "gagarin", "tereshkova")

        val gridListings = List(24) { index ->
            val location = locations[index % locations.size]
            val categoryId = categories[index % categories.size]
            Listing(
                id = "listing-$index",
                title = titles[index % titles.size],
                priceUsd = 80 + index * 15,
                priceRub = 6_700 + index * 1_200,
                location = location.third,
                categoryId = categoryId,
                countryId = location.first,
                regionId = location.second,
                totalImages = 3 + (index % 4),
                sellerId = sellerIds[index % sellerIds.size],
            )
        }

        val favoriteListings = listOf(
            listing("favorite-0", "Number Nine Hoodie", 150, 12_570, ruLocations[1], "clothes", sellerId = "niko"),
            listing("favorite-1", "Nike Air Max Sneakers", 90, 7_540, ruLocations[0], "clothes", sellerId = "yeltsin"),
            listing("favorite-2", "Burberry Coat", 280, 23_450, ruLocations[1], "clothes", sellerId = "lincoln"),
            listing("favorite-3", "Louis Vuitton Bag", 520, 43_550, ruLocations[2], "clothes", sellerId = "alex"),
            listing("favorite-4", "Rolex Submariner Watch", 8_500, 711_500, ruLocations[0], "realty", sellerId = "alex"),
            listing("favorite-5", "Stone Island Jacket", 340, 28_480, ruLocations[1], "clothes", sellerId = "niko"),
            listing("favorite-6", "AirPods Pro", 180, 15_080, ruLocations[1], "electronics", sellerId = "gagarin"),
            listing("favorite-7", "Tiffany Ring", 1_200, 100_500, ruLocations[0], "realty", sellerId = "tereshkova"),
            listing("favorite-8", "BMW X5", 41_000, 3_430_000, ruLocations[0], "auto", totalImages = 8, sellerId = "alex"),
            listing("favorite-9", "Corner Sofa", 175, 14_650, ruLocations[1], "furniture", sellerId = "pushkin"),
        ).map { it.copy(isFavorite = true) }

        val profileActive = List(8) { index ->
            listing(
                id = "active-$index",
                title = "Number Nine Hoodie",
                priceUsd = 150,
                priceRub = 12_570,
                location = ruLocations[1],
                categoryId = "clothes",
                isFavorite = index == 1,
                sellerId = "alex",
            )
        }

        val profileArchive = listOf(
            listing("archive-0", "Nike Air Max Sneakers", 90, 7_540, ruLocations[0], "clothes", isFavorite = true, sellerId = "alex"),
            listing("archive-1", "iPhone 13 Pro 256GB", 620, 51_900, ruLocations[2], "electronics", sellerId = "alex"),
            listing("archive-2", "Burberry Coat", 280, 23_450, ruLocations[1], "clothes", isFavorite = true, sellerId = "alex"),
            listing("archive-3", "Trek Bicycle", 410, 34_300, ruLocations[1], "hobby", sellerId = "alex"),
            listing("archive-4", "Corner Sofa", 175, 14_650, ruLocations[1], "furniture", sellerId = "alex"),
            listing("archive-5", "MacBook Air M2", 890, 74_500, ruLocations[0], "electronics", isFavorite = true, sellerId = "alex"),
        )

        return gridListings + favoriteListings + profileActive + profileArchive
    }

    private fun listing(
        id: String,
        title: String,
        priceUsd: Int,
        priceRub: Int,
        location: Triple<String, String, String>,
        categoryId: String,
        totalImages: Int = 4,
        isFavorite: Boolean = false,
        sellerId: String = "alex",
    ): Listing = Listing(
        id = id,
        title = title,
        priceUsd = priceUsd,
        priceRub = priceRub,
        location = location.third,
        categoryId = categoryId,
        countryId = location.first,
        regionId = location.second,
        totalImages = totalImages,
        isFavorite = isFavorite,
        sellerId = sellerId,
    )
}
