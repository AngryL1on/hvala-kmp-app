package tech.appard.hvala.shared.feature.settings.data.di

import org.koin.dsl.module
import tech.appard.hvala.shared.feature.settings.data.repository.LocaleRepositoryImpl
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository

val settingsDataModule = module {
    single<LocaleRepository> { LocaleRepositoryImpl() }
}
