package tech.appard.hvala.shared.feature.profile.data.repository

import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.profile.data.mapper.toDomain
import tech.appard.hvala.shared.feature.profile.data.source.ProfileJsonDataSource
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile
import tech.appard.hvala.shared.feature.profile.domain.repository.ProfileOverviewRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

internal class JsonProfileOverviewRepository(
    private val dataSource: ProfileJsonDataSource,
) : ProfileOverviewRepository {
    private var profileOverview: ProfileOverview? = null

    override suspend fun ensureLoaded() {
        if (profileOverview != null) return
        profileOverview = dataSource.profileOverview().toDomain()
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
    private val dataSource: ProfileJsonDataSource,
    private val listingsRepository: ListingsRepository,
) : SellerRepository {
    private var sellers: Map<String, SellerProfile> = emptyMap()

    override suspend fun ensureLoaded() {
        if (sellers.isNotEmpty()) return
        listingsRepository.ensureLoaded()
        sellers = dataSource.sellers().sellers.associate { seller ->
            seller.id to seller.toDomain()
        }
    }

    override fun getSellerById(id: String): SellerProfile? = sellers[id]

    override fun getListingsForSeller(sellerId: String): List<Listing> =
        listingsRepository.listings.value
            .filter { it.sellerId == sellerId }
}
