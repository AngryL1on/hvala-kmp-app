package tech.appard.hvala.shared.feature.profile.domain.repository

import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile

interface ProfileOverviewRepository {
    suspend fun ensureLoaded()

    fun getProfileOverview(): ProfileOverview
}

interface SellerRepository {
    suspend fun ensureLoaded()

    fun getSellerById(id: String): SellerProfile?

    fun getListingsForSeller(sellerId: String): List<Listing>
}
