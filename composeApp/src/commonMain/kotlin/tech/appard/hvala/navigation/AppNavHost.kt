package tech.appard.hvala.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import tech.appard.hvala.shared.feature.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val authRepository = koinInject<AuthRepository>()
    val authStateHolder = koinInject<AuthStateHolder>()
    val profileStateHolder = koinInject<ProfileStateHolder>()
    val profileState by profileStateHolder.state.collectAsState()
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentRoute

    val onSessionEnd: () -> Unit = {
        scope.launch {
            authRepository.signOut()
            profileStateHolder.reset()
            authStateHolder.reset()
            navController.navigateToRoot(Route.Auth)
        }
    }

    val appBarState = rememberHvalaAppBarState(
        title = when (currentRoute) {
            Route.Profile -> "Профиль"
            Route.Settings -> "Настройки"
            else -> null
        },
        showBackButton = currentRoute == Route.Settings,
        centerTitle = currentRoute == Route.Profile || currentRoute == Route.Settings,
        showSettingsButton = currentRoute == Route.Profile,
    )

    val usesScreenBackground = currentRoute == Route.Profile || currentRoute == Route.Settings

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (usesScreenBackground) ScreenBackground else White,
        topBar = {
            HvalaAppBar(
                state = appBarState,
                onBackClick = { navController.back() },
                onSettingsClick = {
                    if (currentRoute == Route.Profile) {
                        navController.navigateTo(Route.Settings)
                    }
                },
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
                Route.Settings -> SettingsScreen(
                    fullName = profileState.profile?.fullName ?: "",
                    email = profileState.profile?.email ?: "",
                    onDeleteAccountClick = onSessionEnd,
                    onLogoutClick = onSessionEnd,
                )
            }
        }
    }
}
