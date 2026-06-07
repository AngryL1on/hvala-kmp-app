package tech.appard.hvala.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.koinInject
import tech.appard.hvala.shared.feature.auth.AuthScreen
import tech.appard.hvala.shared.feature.auth.AuthStateHolder
import tech.appard.hvala.shared.feature.auth.AuthUiEvent
import tech.appard.hvala.shared.feature.profile.ProfileScreen
import tech.appard.hvala.shared.feature.profile.ProfileStateHolder
import tech.appard.hvala.shared.feature.profile.ProfileUiEvent

@Composable
fun AppNavHost(
    navController: NavController,
) {
    val authStateHolder = koinInject<AuthStateHolder>()
    val profileStateHolder = koinInject<ProfileStateHolder>()

    LaunchedEffect(authStateHolder) {
        authStateHolder.events.collect { event ->
            when (event) {
                AuthUiEvent.Authenticated -> navController.navigateTo(Route.Profile)
            }
        }
    }

    LaunchedEffect(profileStateHolder) {
        profileStateHolder.events.collect { event ->
            when (event) {
                ProfileUiEvent.LoggedOut -> {
                    authStateHolder.reset()
                    navController.navigateTo(Route.Auth)
                }
            }
        }
    }

    when (navController.currentRoute) {
        is Route.Auth -> AuthScreen(stateHolder = authStateHolder)
        is Route.Profile -> ProfileScreen(stateHolder = profileStateHolder)
    }
}
