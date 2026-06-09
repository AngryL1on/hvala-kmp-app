package tech.appard.hvala.shared.feature.settings.data.di

import org.koin.dsl.module
import tech.appard.hvala.shared.feature.settings.data.repository.LocaleRepositoryImpl
import tech.appard.hvala.shared.feature.settings.data.repository.NotificationPreferencesRepositoryImpl
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository
import tech.appard.hvala.shared.feature.settings.domain.repository.NotificationPreferencesRepository

val settingsDataModule = module {
    single<LocaleRepository> { LocaleRepositoryImpl(get()) }
    single<NotificationPreferencesRepository> { NotificationPreferencesRepositoryImpl(get()) }
}
