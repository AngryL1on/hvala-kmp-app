package tech.appard.hvala.navigation

/**
 * Типобезопасные маршруты приложения (KMP-совместимая навигация без AndroidX Navigation).
 */
sealed interface Route {
    data object Auth : Route
    data object Profile : Route
    data object Settings : Route
}
