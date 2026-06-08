package tech.appard.hvala.navigation

enum class NavTransition {
    Forward,
    Back,
    Tab,
    Root,
    Modal,
    FabModal,
}

internal fun Route.tabOrder(): Int? = when (this) {
    Route.Listings -> 0
    Route.Write -> 1
    Route.Favorites -> 2
    Route.Profile -> 3
    else -> null
}

internal fun Route.isModal(): Boolean = when (this) {
    Route.Auth,
    Route.CreateListing,
    -> true
    is Route.Chat -> false
    else -> false
}
