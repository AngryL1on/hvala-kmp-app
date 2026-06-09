package tech.appard.hvala.shared.feature.settings.presentation.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsViewModel

val settingsFeatureModule = module {
    singleOf(::SettingsViewModel)
}
