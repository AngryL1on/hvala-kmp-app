package tech.appard.hvala.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tech.appard.hvala.shared.core.contracts.repository.AuthRepository
import tech.appard.hvala.shared.feature.auth.AuthScreen
import tech.appard.hvala.shared.feature.auth.AuthStateHolder
import tech.appard.hvala.shared.feature.profile.ProfileScreen
import tech.appard.hvala.shared.feature.profile.ProfileStateHolder

@Composable
fun AppNavHost(
    navController: NavController,
) {
    val authRepository = koinInject<AuthRepository>()
    val authStateHolder = koinInject<AuthStateHolder>()
    val profileStateHolder = koinInject<ProfileStateHolder>()
    val scope = rememberCoroutineScope()

    when (navController.currentRoute) {
        is Route.Auth -> AuthScreen(
            stateHolder = authStateHolder,
            onAuthenticated = { navController.navigateTo(Route.Profile) },
        )
        is Route.Profile -> ProfileScreen(
            stateHolder = profileStateHolder,
            onLogout = {
                scope.launch {
                    authRepository.signOut()
                    profileStateHolder.reset()
                    authStateHolder.reset()
                    navController.navigateTo(Route.Auth)
                }
            },
        )
    }
}
