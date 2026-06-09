package tech.appard.hvala.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.bind
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.auth.domain.repository.AuthRepository
import tech.appard.hvala.shared.feature.auth.domain.repository.ProfileRepository
import tech.appard.hvala.shared.core.data.network.NetworkClient
import tech.appard.hvala.shared.core.data.network.repository.AuthRepositoryImpl
import tech.appard.hvala.shared.core.data.network.repository.ProfileRepositoryImpl
import tech.appard.hvala.shared.feature.auth.di.authFeatureModule
import tech.appard.hvala.shared.feature.favorites.di.favoritesFeatureModule
import tech.appard.hvala.shared.feature.listings.data.di.listingsDataModule
import tech.appard.hvala.shared.feature.listings.domain.di.listingsDomainModule
import tech.appard.hvala.shared.feature.listings.di.listingsFeatureModule
import tech.appard.hvala.shared.feature.messages.data.di.messagesDataModule
import tech.appard.hvala.shared.feature.messages.domain.di.messagesDomainModule
import tech.appard.hvala.shared.feature.messages.di.messagesFeatureModule
import tech.appard.hvala.shared.feature.profile.data.di.profileDataModule
import tech.appard.hvala.shared.feature.profile.domain.di.profileDomainModule
import tech.appard.hvala.shared.feature.profile.di.profileFeatureModule
import tech.appard.hvala.shared.feature.settings.data.di.settingsDataModule
import tech.appard.hvala.shared.feature.settings.domain.di.settingsDomainModule
import tech.appard.hvala.shared.feature.settings.presentation.di.settingsFeatureModule

val appModule = module {
    singleOf(::NetworkClient)
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }
    includes(
        listingsDataModule,
        profileDataModule,
        messagesDataModule,
        listingsDomainModule,
        profileDomainModule,
        messagesDomainModule,
        settingsDataModule,
        settingsDomainModule,
        authFeatureModule,
        profileFeatureModule,
        listingsFeatureModule,
        messagesFeatureModule,
        favoritesFeatureModule,
        settingsFeatureModule,
    )
}
