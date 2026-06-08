package tech.appard.hvala.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module
import tech.appard.hvala.shared.core.contracts.repository.AuthRepository
import tech.appard.hvala.shared.core.contracts.repository.ProfileRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient
import tech.appard.hvala.shared.core.data.network.repository.AuthRepositoryImpl
import tech.appard.hvala.shared.core.data.network.repository.ProfileRepositoryImpl
import tech.appard.hvala.shared.feature.auth.authFeatureModule
import tech.appard.hvala.shared.feature.listings.listingsFeatureModule
import tech.appard.hvala.shared.feature.messages.messagesFeatureModule
import tech.appard.hvala.shared.feature.profile.profileFeatureModule

val appModule = module {
    singleOf(::NetworkClient)
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }
    includes(authFeatureModule, profileFeatureModule, listingsFeatureModule, messagesFeatureModule)
}
