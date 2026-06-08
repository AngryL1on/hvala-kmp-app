package tech.appard.hvala.shared.feature.listings.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.listings.presentation.CreateListingStateHolder
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailViewModel
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingsViewModel

val listingsFeatureModule = module {
    singleOf(::ListingsViewModel)
    singleOf(::CreateListingStateHolder)
    singleOf(::ListingDetailViewModel)
}
