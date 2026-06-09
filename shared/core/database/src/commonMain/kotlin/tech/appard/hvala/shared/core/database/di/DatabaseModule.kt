package tech.appard.hvala.shared.core.database.di

import org.koin.dsl.module
import tech.appard.hvala.shared.core.database.HvalaDatabase
import tech.appard.hvala.shared.core.database.driver.DatabaseDriverFactory

val databaseModule = module {
    single { get<DatabaseDriverFactory>().createDriver() }
    single { HvalaDatabase(get()) }
}
