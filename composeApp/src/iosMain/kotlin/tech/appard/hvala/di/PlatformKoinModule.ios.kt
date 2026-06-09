package tech.appard.hvala.di

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.appard.hvala.shared.core.database.driver.DatabaseDriverFactory

internal actual fun platformKoinModule(): Module = module {
    single { DatabaseDriverFactory() }
}
