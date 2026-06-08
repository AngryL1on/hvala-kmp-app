package tech.appard.hvala.navigation

import androidx.compose.runtime.Composable
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

    fun navigateTo(route: Route) {
        state.value = state.value.copy(
            backStack = state.value.backStack + state.value.current,
            current = route,
        )
    }

    fun navigateToRoot(route: Route) {
        state.value = NavState(
            current = route,
            backStack = emptyList(),
        )
    }

    fun back(): Boolean {
        val backStack = state.value.backStack
        if (backStack.isEmpty()) return false
        state.value = state.value.copy(
            backStack = backStack.dropLast(1),
            current = backStack.last(),
        )
        return true
    }
}

data class NavState(
    val current: Route,
    val backStack: List<Route>,
)

@Composable
fun rememberNavController(initialRoute: Route = Route.Auth): NavController {
    val state = remember { mutableStateOf(NavState(initialRoute, emptyList())) }
    return remember { NavController(state) }
}
