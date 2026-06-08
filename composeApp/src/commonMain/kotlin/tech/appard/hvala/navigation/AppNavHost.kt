package tech.appard.hvala.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import tech.appard.hvala.shared.core.ui.components.appbars.HvalaAppBar
import tech.appard.hvala.shared.core.ui.components.appbars.rememberHvalaAppBarState
import tech.appard.hvala.shared.core.ui.components.bottomnav.BottomNavItem
import tech.appard.hvala.shared.core.ui.components.bottomnav.HvalaBottomNavBar
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.ui.utils.HvalaStatusBarEffect
import tech.appard.hvala.shared.feature.auth.AuthScreen
import tech.appard.hvala.shared.feature.auth.AuthStateHolder
import tech.appard.hvala.shared.feature.favorites.FavoritesScreen
import tech.appard.hvala.shared.feature.favorites.FavoritesStateHolder
import tech.appard.hvala.shared.feature.listings.ListingsScreen
import tech.appard.hvala.shared.feature.listings.ListingsStateHolder
import tech.appard.hvala.shared.feature.messages.ChatScreen
import tech.appard.hvala.shared.feature.messages.MessagesScreen
import tech.appard.hvala.shared.feature.messages.MessagesStateHolder
import tech.appard.hvala.shared.feature.profile.ProfileScreen
import tech.appard.hvala.shared.feature.profile.ProfileStateHolder
import tech.appard.hvala.shared.feature.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val authStateHolder = koinInject<AuthStateHolder>()
    val listingsStateHolder = koinInject<ListingsStateHolder>()
    val messagesStateHolder = koinInject<MessagesStateHolder>()
    val profileStateHolder = koinInject<ProfileStateHolder>()
    val favoritesStateHolder = koinInject<FavoritesStateHolder>()
    val profileState by profileStateHolder.state.collectAsState()
    val chatState by messagesStateHolder.chatState.collectAsState()
    val isAuthenticated by authStateHolder.isAuthenticated.collectAsState()
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentRoute
    val currentScreen = navController.currentScreen
    val showsBottomNav = currentRoute.showsBottomNav(isAuthenticated)
    val selectedBottomNavItem = currentRoute.toBottomNavItem() ?: BottomNavItem.Listings

    LaunchedEffect(isAuthenticated, currentRoute) {
        if (!isAuthenticated && currentRoute.requiresAuthentication()) {
            navController.navigateToRoot(Route.Listings)
        }
    }

    val onSessionEnd: () -> Unit = {
        scope.launch {
            authStateHolder.signOut()
            profileStateHolder.reset()
            favoritesStateHolder.reset()
            navController.navigateToRoot(Route.Listings)
        }
    }

    val showAppBar = when (currentRoute) {
        Route.Auth -> true
        Route.Profile,
        Route.Settings,
        Route.CreateListing,
        Route.Write,
        Route.Favorites,
        is Route.Chat,
        -> isAuthenticated
        else -> false
    }

    val appBarState = rememberHvalaAppBarState(
        title = when (currentRoute) {
            Route.Profile -> "Профиль"
            Route.Settings -> "Настройки"
            Route.CreateListing -> "Новое объявление"
            Route.Write -> "Сообщения"
            Route.Favorites -> "Избранное"
            is Route.Chat -> chatState.thread?.participantName ?: "Чат"
            else -> null
        },
        showBackButton = when (currentRoute) {
            Route.Auth,
            Route.Settings,
            Route.CreateListing,
            is Route.Chat,
            -> true
            else -> false
        },
        centerTitle = currentRoute == Route.Profile ||
            currentRoute == Route.Settings ||
            currentRoute == Route.CreateListing ||
            currentRoute == Route.Write ||
            currentRoute == Route.Favorites,
        showSettingsButton = currentRoute == Route.Profile,
        leadingAvatarColorArgb = when (currentRoute) {
            is Route.Chat -> chatState.thread?.avatarColorArgb
            else -> null
        },
    )

    val usesScreenBackground = when (currentRoute) {
        Route.Profile,
        Route.Settings,
        Route.CreateListing,
        Route.Listings,
        Route.Write,
        Route.Favorites,
        is Route.Chat,
        -> true
        else -> false
    }

    val usesPrimaryStatusBar = currentRoute == Route.Listings

    HvalaStatusBarEffect(
        statusBarColor = if (usesPrimaryStatusBar) PrimaryMain else White,
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = if (usesScreenBackground) ScreenBackground else White,
        contentWindowInsets = WindowInsets.safeDrawing.only(
            if (usesPrimaryStatusBar) {
                WindowInsetsSides.Horizontal
            } else {
                WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            },
        ),
        topBar = {
            AnimatedVisibility(
                visible = showAppBar,
                enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { -it / 2 },
                exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { -it / 2 },
            ) {
                HvalaAppBar(
                    state = appBarState,
                    onBackClick = { navController.back() },
                    onSettingsClick = {
                        if (currentRoute == Route.Profile) {
                            navController.navigateTo(Route.Settings)
                        }
                    },
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showsBottomNav,
                modifier = Modifier.fillMaxWidth(),
                enter = fadeIn(tween(220)) + slideInVertically(tween(220)) { it / 2 },
                exit = fadeOut(tween(180)) + slideOutVertically(tween(180)) { it / 2 },
            ) {
                HvalaBottomNavBar(
                    selectedItem = selectedBottomNavItem,
                    onItemSelected = { item ->
                        item.toRoute()?.let { route ->
                            navController.switchTab(route)
                        }
                    },
                    onAddClick = {
                        navController.navigateFromFab()
                    },
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    screenTransitionSpec(
                        transition = targetState.transition,
                        from = initialState.route,
                        to = targetState.route,
                    )
                },
                contentKey = { screen ->
                    when (val route = screen.route) {
                        is Route.Chat -> route.threadId
                        else -> route::class
                    }
                },
                label = "screenTransition",
            ) { screen ->
                when (val route = screen.route) {
                    Route.Auth -> AuthScreen(
                        stateHolder = authStateHolder,
                        onAuthenticated = { navController.navigateToRoot(Route.Listings) },
                    )
                    Route.Listings -> ListingsScreen(
                        stateHolder = listingsStateHolder,
                        showGuestLoginButton = !isAuthenticated,
                        onLoginClick = { navController.navigateTo(Route.Auth) },
                    )
                    Route.Write -> MessagesScreen(
                        stateHolder = messagesStateHolder,
                        onChatClick = { threadId ->
                            navController.navigateTo(Route.Chat(threadId))
                        },
                    )
                    is Route.Chat -> ChatScreen(
                        threadId = route.threadId,
                        stateHolder = messagesStateHolder,
                    )
                    Route.Favorites -> FavoritesScreen(
                        stateHolder = favoritesStateHolder,
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
                    Route.CreateListing -> MainPlaceholderScreen(title = "Создание объявления")
                }
            }
        }
    }
}
