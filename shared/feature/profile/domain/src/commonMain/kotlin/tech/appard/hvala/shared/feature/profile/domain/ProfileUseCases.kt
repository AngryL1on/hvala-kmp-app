package tech.appard.hvala.shared.feature.profile.domain

import tech.appard.hvala.shared.feature.listings.domain.model.Listing
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository
import tech.appard.hvala.shared.feature.profile.domain.model.ProfileOverview
import tech.appard.hvala.shared.feature.profile.domain.model.SellerProfile
import tech.appard.hvala.shared.feature.profile.domain.repository.ProfileOverviewRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

class GetProfileOverviewUseCase(
    private val profileOverviewRepository: ProfileOverviewRepository,
    private val listingsRepository: ListingsRepository,
) {
    suspend operator fun invoke(): ProfileListingsBundle {
        profileOverviewRepository.ensureLoaded()
        listingsRepository.ensureLoaded()
        val overview = profileOverviewRepository.getProfileOverview()
        return ProfileListingsBundle(
            overview = overview,
            activeListings = listingsRepository.getActiveProfileListings(),
            archiveListings = listingsRepository.getArchiveProfileListings(),
        )
    }
}

data class ProfileListingsBundle(
    val overview: ProfileOverview,
    val activeListings: List<Listing>,
    val archiveListings: List<Listing>,
)

class GetSellerProfileUseCase(
    private val sellerRepository: SellerRepository,
) {
    suspend operator fun invoke(sellerId: String): SellerProfileBundle? {
        sellerRepository.ensureLoaded()
        val seller = sellerRepository.getSellerById(sellerId) ?: return null
        return SellerProfileBundle(
            seller = seller,
            listings = sellerRepository.getListingsForSeller(sellerId),
        )
    }
}

data class SellerProfileBundle(
    val seller: SellerProfile,
    val listings: List<Listing>,
)
