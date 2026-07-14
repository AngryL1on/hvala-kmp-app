package tech.appard.hvala.shared.feature.listings.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.listings.domain.CreateListingUseCase
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.feature.listings.domain.GetFavoriteListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.GetListingByIdUseCase
import tech.appard.hvala.shared.feature.listings.domain.ObserveListingsUseCase
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase

val listingsDomainModule = module {
    factoryOf(::ObserveListingsUseCase)
    factoryOf(::GetListingByIdUseCase)
    factoryOf(::ToggleListingFavoriteUseCase)
    factoryOf(::GetFavoriteListingsUseCase)
    factoryOf(::GetCatalogDefaultsUseCase)
    factoryOf(::CreateListingUseCase)
}
