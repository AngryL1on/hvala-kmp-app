package tech.appard.hvala.shared.core.database

object DatabaseSeedKeys {
    const val LISTINGS = "seed_listings_v2"
    const val SELLERS = "seed_sellers"
    const val PROFILE_OVERVIEW = "seed_profile_overview"
    const val REVIEWS = "seed_reviews"
    const val MESSAGES = "seed_messages_v2"
}

fun HvalaDatabase.isSeeded(key: String): Boolean =
    appMetaQueries.getMetaValue(key).executeAsOneOrNull() == "1"

fun HvalaDatabase.markSeeded(key: String) {
    appMetaQueries.setMetaValue(key, "1")
}
