package tech.appard.hvala.shared.feature.listings

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val listingsFeatureModule = module {
    singleOf(::ListingsViewModel)
    singleOf(::CreateListingStateHolder)
    singleOf(::ListingDetailViewModel)
}
