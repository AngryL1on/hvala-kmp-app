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
import tech.appard.hvala.shared.feature.auth.presentation.screens.AuthScreen
import tech.appard.hvala.shared.feature.auth.presentation.AuthViewModel
import tech.appard.hvala.shared.feature.auth.presentation.screens.RegistrationScreen
import tech.appard.hvala.shared.feature.favorites.presentation.screens.FavoritesScreen
import tech.appard.hvala.shared.feature.favorites.presentation.viewmodels.FavoritesViewModel
import tech.appard.hvala.shared.feature.listings.presentation.screens.CreateListingScreen
import tech.appard.hvala.shared.feature.listings.presentation.CreateListingViewModel
import tech.appard.hvala.shared.feature.listings.presentation.screens.ListingDetailScreen
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailViewModel
import tech.appard.hvala.shared.feature.listings.presentation.screens.ListingsScreen
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingsViewModel
import tech.appard.hvala.shared.feature.messages.presentation.screens.ChatScreen
import tech.appard.hvala.shared.feature.messages.presentation.screens.MessagesScreen
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.MessagesViewModel
import tech.appard.hvala.shared.feature.messages.presentation.mapper.resolvedSellerId
import tech.appard.hvala.shared.feature.profile.presentation.screens.EditProfileScreen
import tech.appard.hvala.shared.feature.profile.presentation.screens.ProfileScreen
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.EditProfileViewModel
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ProfileViewModel
import tech.appard.hvala.shared.feature.profile.presentation.screens.ReviewsScreen
import tech.appard.hvala.shared.feature.profile.presentation.screens.SellerProfileScreen
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.ReviewsViewModel
import tech.appard.hvala.shared.feature.profile.presentation.viewmodels.SellerProfileViewModel
import tech.appard.hvala.shared.feature.listings.domain.ToggleListingFavoriteUseCase
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.settings.presentation.screens.ContactsScreen
import tech.appard.hvala.shared.feature.settings.presentation.screens.HelpScreen
import tech.appard.hvala.shared.feature.settings.presentation.screens.InformationScreen
import tech.appard.hvala.shared.feature.settings.presentation.screens.PrivacyPolicyScreen
import tech.appard.hvala.shared.feature.settings.presentation.screens.SettingsScreen
import tech.appard.hvala.shared.feature.settings.presentation.viewmodels.SettingsViewModel

@Composable
fun AppNavHost(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val authViewModel = koinInject<AuthViewModel>()
    val listingsViewModel = koinInject<ListingsViewModel>()
    val createListingViewModel = koinInject<CreateListingViewModel>()
    val listingDetailViewModel = koinInject<ListingDetailViewModel>()
    val messagesViewModel = koinInject<MessagesViewModel>()
    val profileViewModel = koinInject<ProfileViewModel>()
    val editProfileViewModel = koinInject<EditProfileViewModel>()
    val sellerProfileViewModel = koinInject<SellerProfileViewModel>()
    val reviewsViewModel = koinInject<ReviewsViewModel>()
    val favoritesViewModel = koinInject<FavoritesViewModel>()
    val settingsViewModel = koinInject<SettingsViewModel>()
    val toggleListingFavoriteUseCase = koinInject<ToggleListingFavoriteUseCase>()
    val profileState by profileViewModel.state.collectAsState()
    val sellerProfileState by sellerProfileViewModel.state.collectAsState()
    val listingsState by listingsViewModel.state.collectAsState()
    val listingDetailState by listingDetailViewModel.state.collectAsState()
    val chatState by messagesViewModel.chatState.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val strings = appStrings()
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

    val onListingClick: (String) -> Unit = { listingId ->
        navController.navigateTo(Route.ListingDetail(listingId))
    }

    val onSellerClick: (String) -> Unit = { sellerId ->
        navController.navigateTo(Route.SellerProfile(sellerId))
    }

    val onListingFavoriteToggle: (String) -> Unit = { listingId ->
        if (!isAuthenticated) {
            navController.navigateTo(Route.Auth)
        } else {
            val currentFavorite = listingDetailState.listing
                ?.takeIf { it.id == listingId }
                ?.isFavorite
                ?: listingsState.allListings.find { it.id == listingId }?.isFavorite
                ?: false

            scope.launch {
                toggleListingFavoriteUseCase(listingId)
                listingDetailViewModel.syncFavorite(!currentFavorite)
            }
        }
    }

    val onReviewsClick: (String) -> Unit = { sellerId ->
        navController.navigateTo(Route.Reviews(sellerId))
    }

    val onSessionEnd: () -> Unit = {
        scope.launch {
            authViewModel.signOut()
            profileViewModel.reset()
            favoritesViewModel.reset()
            listingDetailViewModel.reset()
            sellerProfileViewModel.reset()
            navController.navigateToRoot(Route.Listings)
        }
    }

    val showAppBar = when (currentRoute) {
        Route.Auth,
        Route.Registration,
        Route.Information,
        Route.PrivacyPolicy,
        Route.Contacts,
        Route.Help,
        is Route.ListingDetail,
        is Route.SellerProfile,
        is Route.Reviews,
        -> true
        Route.Profile,
        Route.Settings,
        Route.EditProfile,
        Route.CreateListing,
        Route.Write,
        Route.Favorites,
        is Route.Chat,
        -> isAuthenticated
        else -> false
    }

    val appBarState = rememberHvalaAppBarState(
        title = when (currentRoute) {
            Route.Registration -> strings.nav.registration
            Route.Information -> strings.information.screenTitle
            Route.PrivacyPolicy -> strings.privacyPolicy.screenTitle
            Route.Contacts -> strings.contacts.screenTitle
            Route.Help -> strings.help.screenTitle
            Route.Profile -> strings.nav.profileTitle
            Route.Settings -> strings.settings.screenTitle
            Route.EditProfile -> strings.settings.editProfile
            Route.CreateListing -> strings.nav.createListing
            is Route.ListingDetail -> listingDetailState.listing?.title ?: strings.common.listing
            is Route.SellerProfile -> sellerProfileState.seller?.name ?: strings.common.seller
            is Route.Reviews -> strings.reviews.screenTitle
            Route.Write -> strings.nav.messages
            Route.Favorites -> strings.nav.favoritesTitle
            is Route.Chat -> chatState.thread?.participantName ?: strings.common.chat
            else -> null
        },
        showBackButton = when (currentRoute) {
            Route.Auth,
            Route.Registration,
            Route.Information,
            Route.PrivacyPolicy,
            Route.Contacts,
            Route.Help,
            Route.Settings,
            Route.EditProfile,
            Route.CreateListing,
            is Route.ListingDetail,
            is Route.SellerProfile,
            is Route.Reviews,
            is Route.Chat,
            -> true
            else -> false
        },
        centerTitle = currentRoute == Route.Registration ||
            currentRoute == Route.Information ||
            currentRoute == Route.PrivacyPolicy ||
            currentRoute == Route.Contacts ||
            currentRoute == Route.Help ||
            currentRoute == Route.Profile ||
            currentRoute == Route.Settings ||
            currentRoute == Route.EditProfile ||
            currentRoute == Route.CreateListing ||
            currentRoute is Route.ListingDetail ||
            currentRoute is Route.SellerProfile ||
            currentRoute is Route.Reviews ||
            currentRoute == Route.Write ||
            currentRoute == Route.Favorites,
        showSettingsButton = currentRoute == Route.Profile,
        leadingAvatarColorArgb = when (currentRoute) {
            is Route.Chat -> chatState.thread?.avatarColorArgb
            else -> null
        },
    )

    val chatTitleClick = when (currentRoute) {
        is Route.Chat -> chatState.thread?.resolvedSellerId()?.let { sellerId ->
            { onSellerClick(sellerId) }
        }
        else -> null
    }

    val usesScreenBackground = when (currentRoute) {
        Route.Profile,
        Route.Settings,
        Route.Information,
        Route.PrivacyPolicy,
        Route.Contacts,
        Route.Help,
        Route.EditProfile,
        Route.CreateListing,
        is Route.ListingDetail,
        is Route.SellerProfile,
        is Route.Reviews,
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
                    onTitleClick = chatTitleClick,
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
                        is Route.ListingDetail -> route.listingId
                        is Route.SellerProfile -> route.sellerId
                        is Route.Reviews -> route.sellerId
                        else -> route::class
                    }
                },
                label = "screenTransition",
            ) { screen ->
                when (val route = screen.route) {
                    Route.Auth -> AuthScreen(
                        viewModel = authViewModel,
                        onAuthenticated = { navController.navigateToRoot(Route.Listings) },
                        onSignUpClick = { navController.navigateTo(Route.Registration) },
                    )
                    Route.Registration -> RegistrationScreen(
                        viewModel = authViewModel,
                        onRegistered = {
                            authViewModel.resetRegistration()
                            profileViewModel.reset()
                            navController.navigateToRoot(Route.Listings)
                        },
                        onTermsClick = { navController.navigateTo(Route.PrivacyPolicy) },
                    )
                    Route.PrivacyPolicy -> PrivacyPolicyScreen()
                    Route.Information -> InformationScreen()
                    Route.Contacts -> ContactsScreen()
                    Route.Help -> HelpScreen()
                    Route.Listings -> ListingsScreen(
                        viewModel = listingsViewModel,
                        showGuestLoginButton = !isAuthenticated,
                        onLoginClick = { navController.navigateTo(Route.Auth) },
                        onListingClick = onListingClick,
                    )
                    Route.Write -> MessagesScreen(
                        viewModel = messagesViewModel,
                        onChatClick = { threadId ->
                            navController.navigateTo(Route.Chat(threadId))
                        },
                    )
                    is Route.Chat -> ChatScreen(
                        threadId = route.threadId,
                        viewModel = messagesViewModel,
                        onListingClick = onListingClick,
                    )
                    Route.Favorites -> FavoritesScreen(
                        viewModel = favoritesViewModel,
                        onListingClick = onListingClick,
                    )
                    Route.Profile -> ProfileScreen(
                        viewModel = profileViewModel,
                        onListingClick = onListingClick,
                        onReviewsClick = {
                            profileState.profile?.id?.let(onReviewsClick)
                        },
                    )
                    Route.Settings -> SettingsScreen(
                        viewModel = settingsViewModel,
                        fullName = profileState.profile?.fullName ?: "",
                        email = profileState.profile?.email ?: "",
                        phone = profileState.profile?.phone ?: "",
                        avatarUrl = profileState.profile?.avatarUrl,
                        onEditProfileClick = { navController.navigateTo(Route.EditProfile) },
                        onAvatarPicked = profileViewModel::updateAvatar,
                        onSessionEnd = onSessionEnd,
                        onInformationClick = { navController.navigateTo(Route.Information) },
                        onPrivacyPolicyClick = { navController.navigateTo(Route.PrivacyPolicy) },
                        onContactsClick = { navController.navigateTo(Route.Contacts) },
                        onHelpClick = { navController.navigateTo(Route.Help) },
                    )
                    Route.EditProfile -> EditProfileScreen(
                        viewModel = editProfileViewModel,
                        onSaved = {
                            profileViewModel.refreshProfile()
                            navController.back()
                        },
                    )
                    Route.CreateListing -> CreateListingScreen(
                        viewModel = createListingViewModel,
                        onSubmitted = { navController.back() },
                    )
                    is Route.ListingDetail -> ListingDetailScreen(
                        listingId = route.listingId,
                        viewModel = listingDetailViewModel,
                        isFavoriteOverride = listingsState.allListings
                            .find { it.id == route.listingId }
                            ?.isFavorite,
                        onFavoriteToggle = onListingFavoriteToggle,
                        onContactClick = {
                            if (!isAuthenticated) {
                                navController.navigateTo(Route.Auth)
                            } else {
                                scope.launch {
                                    messagesViewModel.openChatForListing(route.listingId)?.let { threadId ->
                                        navController.navigateTo(Route.Chat(threadId))
                                    }
                                }
                            }
                        },
                        onSellerClick = onSellerClick,
                    )
                    is Route.SellerProfile -> SellerProfileScreen(
                        sellerId = route.sellerId,
                        viewModel = sellerProfileViewModel,
                        onListingClick = onListingClick,
                        onReviewsClick = { onReviewsClick(route.sellerId) },
                    )
                    is Route.Reviews -> ReviewsScreen(
                        sellerId = route.sellerId,
                        viewModel = reviewsViewModel,
                    )
                }
            }
        }
    }
}
