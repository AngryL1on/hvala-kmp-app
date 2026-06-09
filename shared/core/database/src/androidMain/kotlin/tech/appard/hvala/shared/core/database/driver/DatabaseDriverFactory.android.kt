package tech.appard.hvala.shared.core.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import tech.appard.hvala.shared.core.database.HvalaDatabase

actual class DatabaseDriverFactory(
    private val context: Context,
) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = HvalaDatabase.Schema,
        context = context,
        name = "hvala.db",
    )
}
