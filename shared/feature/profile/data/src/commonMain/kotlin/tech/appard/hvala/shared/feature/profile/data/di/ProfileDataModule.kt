package tech.appard.hvala.shared.feature.profile.data.di

import org.koin.dsl.module
import tech.appard.hvala.shared.feature.profile.data.repository.JsonProfileOverviewRepository
import tech.appard.hvala.shared.feature.profile.data.repository.JsonSellerRepository
import tech.appard.hvala.shared.feature.profile.data.source.ProfileJsonDataSource
import tech.appard.hvala.shared.feature.profile.domain.repository.ProfileOverviewRepository
import tech.appard.hvala.shared.feature.profile.domain.repository.SellerRepository

val profileDataModule = module {
    single { ProfileJsonDataSource() }
    single<ProfileOverviewRepository> { JsonProfileOverviewRepository(get()) }
    single<SellerRepository> { JsonSellerRepository(get(), get()) }
}
