package tech.appard.hvala.navigation

import tech.appard.hvala.shared.core.ui.components.bottomnav.BottomNavItem

/**
 * Типобезопасные маршруты приложения (KMP-совместимая навигация без AndroidX Navigation).
 */
sealed interface Route {
    data object Auth : Route
    data object Registration : Route
    data object Listings : Route
    data object Write : Route
    data object Favorites : Route
    data object Profile : Route
    data object Settings : Route
    data object EditProfile : Route
    data object CreateListing : Route
    data class ListingDetail(val listingId: String) : Route
    data class SellerProfile(val sellerId: String) : Route
    data class Reviews(val sellerId: String) : Route
    data class Chat(val threadId: String) : Route
}

fun Route.showsBottomNav(isAuthenticated: Boolean): Boolean {
    if (!isAuthenticated) return false
    return when (this) {
        Route.Listings,
        Route.Write,
        Route.Favorites,
        Route.Profile,
        -> true
        else -> false
    }
}

fun Route.requiresAuthentication(): Boolean = when (this) {
    Route.Write,
    Route.Favorites,
    Route.Profile,
    Route.Settings,
    Route.EditProfile,
    Route.CreateListing,
    is Route.Chat,
    -> true
    else -> false
}

fun Route.toBottomNavItem(): BottomNavItem? = when (this) {
    Route.Listings -> BottomNavItem.Listings
    Route.Write -> BottomNavItem.Write
    Route.Favorites -> BottomNavItem.Favorites
    Route.Profile,
    Route.Settings,
    -> BottomNavItem.Profile
    is Route.Chat -> BottomNavItem.Write
    else -> null
}

fun BottomNavItem.toRoute(): Route? = when (this) {
    BottomNavItem.Listings -> Route.Listings
    BottomNavItem.Write -> Route.Write
    BottomNavItem.Favorites -> Route.Favorites
    BottomNavItem.Profile -> Route.Profile
    BottomNavItem.Add -> null
}
