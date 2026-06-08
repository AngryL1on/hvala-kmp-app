package tech.appard.hvala.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Контроллер навигации с back stack (KMP).
 */
class NavController(
    private val state: MutableState<NavState>,
) {
    val currentRoute: Route get() = state.value.current

    val currentTransition: NavTransition get() = state.value.transition

    val currentScreen: NavScreen
        get() = NavScreen(
            route = state.value.current,
            transition = state.value.transition,
        )

    fun navigateTo(route: Route) {
        val transition = when (route) {
            Route.Auth -> NavTransition.Modal
            else -> NavTransition.Forward
        }
        state.value = state.value.copy(
            backStack = state.value.backStack + state.value.current,
            current = route,
            transition = transition,
        )
    }

    fun navigateFromFab(route: Route = Route.CreateListing) {
        state.value = state.value.copy(
            backStack = state.value.backStack + state.value.current,
            current = route,
            transition = NavTransition.FabModal,
        )
    }

    fun navigateToRoot(route: Route) {
        state.value = NavState(
            current = route,
            backStack = emptyList(),
            transition = NavTransition.Root,
        )
    }

    fun switchTab(route: Route) {
        val isMainTab = when (route) {
            Route.Listings,
            Route.Write,
            Route.Favorites,
            Route.Profile,
            -> true
            else -> false
        }
        if (!isMainTab) return
        if (state.value.current == route) return
        state.value = NavState(
            current = route,
            backStack = emptyList(),
            transition = NavTransition.Tab,
        )
    }

    fun back(): Boolean {
        val backStack = state.value.backStack
        if (backStack.isEmpty()) return false
        state.value = state.value.copy(
            backStack = backStack.dropLast(1),
            current = backStack.last(),
            transition = NavTransition.Back,
        )
        return true
    }
}

@Immutable
data class NavScreen(
    val route: Route,
    val transition: NavTransition,
)

data class NavState(
    val current: Route,
    val backStack: List<Route>,
    val transition: NavTransition = NavTransition.Root,
)

@Composable
fun rememberNavController(initialRoute: Route = Route.Auth): NavController {
    val state = remember { mutableStateOf(NavState(initialRoute, emptyList())) }
    return remember { NavController(state) }
}
