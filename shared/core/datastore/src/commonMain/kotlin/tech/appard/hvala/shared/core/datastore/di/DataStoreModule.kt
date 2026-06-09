package tech.appard.hvala.shared.core.datastore.di

import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import tech.appard.hvala.shared.core.datastore.PreferencesStore
import tech.appard.hvala.shared.core.datastore.SettingsPreferencesStore
import tech.appard.hvala.shared.core.datastore.session.SessionStorage

val datastoreModule = module {
    single<Settings> { Settings() }
    single<PreferencesStore> { SettingsPreferencesStore(get()) }
    singleOf(::SessionStorage)
}
