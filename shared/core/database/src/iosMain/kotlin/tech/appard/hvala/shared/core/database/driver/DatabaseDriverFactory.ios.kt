package tech.appard.hvala.shared.core.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import tech.appard.hvala.shared.core.database.HvalaDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        schema = HvalaDatabase.Schema,
        name = "hvala.db",
    )
}
