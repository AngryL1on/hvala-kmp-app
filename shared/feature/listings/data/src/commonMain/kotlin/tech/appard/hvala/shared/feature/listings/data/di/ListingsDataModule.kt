package tech.appard.hvala.shared.feature.listings.data.di

import org.koin.dsl.module
import tech.appard.hvala.shared.feature.listings.data.repository.JsonCatalogRepository
import tech.appard.hvala.shared.feature.listings.data.repository.JsonListingsRepository
import tech.appard.hvala.shared.feature.listings.data.source.ListingsJsonDataSource
import tech.appard.hvala.shared.feature.listings.domain.repository.CatalogRepository
import tech.appard.hvala.shared.feature.listings.domain.repository.ListingsRepository

val listingsDataModule = module {
    single { ListingsJsonDataSource() }
    single<ListingsRepository> { JsonListingsRepository(get(), get(), get()) }
    single<CatalogRepository> { JsonCatalogRepository(get()) }
}
