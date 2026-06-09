package tech.appard.hvala.shared.feature.settings.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.settings.domain.GetAppLanguageUseCase
import tech.appard.hvala.shared.feature.settings.domain.GetNotificationPreferencesUseCase
import tech.appard.hvala.shared.feature.settings.domain.SetAppLanguageUseCase
import tech.appard.hvala.shared.feature.settings.domain.SetNotificationPreferencesUseCase

val settingsDomainModule = module {
    factoryOf(::GetAppLanguageUseCase)
    factoryOf(::SetAppLanguageUseCase)
    factoryOf(::GetNotificationPreferencesUseCase)
    factoryOf(::SetNotificationPreferencesUseCase)
}
