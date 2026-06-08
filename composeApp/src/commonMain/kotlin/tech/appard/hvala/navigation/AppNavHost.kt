package tech.appard.hvala.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tech.appard.hvala.shared.core.contracts.repository.AuthRepository
import tech.appard.hvala.shared.core.ui.components.appbars.HvalaAppBar
import tech.appard.hvala.shared.core.ui.components.appbars.rememberHvalaAppBarState
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.auth.AuthScreen
import tech.appard.hvala.shared.feature.auth.AuthStateHolder
import tech.appard.hvala.shared.feature.profile.ProfileScreen
import tech.appard.hvala.shared.feature.profile.ProfileStateHolder

@Composable
fun AppNavHost(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val authRepository = koinInject<AuthRepository>()
    val authStateHolder = koinInject<AuthStateHolder>()
    val profileStateHolder = koinInject<ProfileStateHolder>()
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentRoute

    val isProfileRoute = currentRoute == Route.Profile

    val onProfileSettingsClick: () -> Unit = {
        scope.launch {
            authRepository.signOut()
            profileStateHolder.reset()
            authStateHolder.reset()
            navController.navigateTo(Route.Auth)
        }
    }

    val appBarState = rememberHvalaAppBarState(
        title = if (isProfileRoute) "Профиль" else null,
        showBackButton = currentRoute == Route.Auth,
        centerTitle = isProfileRoute,
        showSettingsButton = isProfileRoute,
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (isProfileRoute) ScreenBackground else White,
        topBar = {
            HvalaAppBar(
                state = appBarState,
                onBackClick = {},
                onSettingsClick = if (isProfileRoute) onProfileSettingsClick else ({ }),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (currentRoute) {
                Route.Auth -> AuthScreen(
                    stateHolder = authStateHolder,
                    onAuthenticated = { navController.navigateTo(Route.Profile) },
                )
                Route.Profile -> ProfileScreen(
                    stateHolder = profileStateHolder,
                )
            }
        }
    }
}
