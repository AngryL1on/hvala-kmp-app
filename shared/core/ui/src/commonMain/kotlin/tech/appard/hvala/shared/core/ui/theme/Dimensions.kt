package tech.appard.hvala.shared.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A class representing dimensions for vertical and horizontal spacing used in the UI layout,
 * following an 8-pixel grid system. The dimensions are defined in `Dp` units to provide
 * consistent spacing across different screen densities and sizes.
 *
 * This class is immutable, and instances of it can be used to define standard spacing values
 * for both vertical and horizontal alignment in a composable UI.
 */
@Immutable
data class Dimensions(
    /**
     * Vertical dimensions
     */
    val verticalXXSmall: Dp = 4.dp,   // 0.5 x base unit
    val verticalXSmall: Dp = 8.dp,    // 1 x base unit
    val verticalSmall: Dp = 12.dp,    // 1.5 x base unit
    val verticalMedium: Dp = 16.dp,   // 2 x base unit
    val verticalLarge: Dp = 24.dp,    // 3 x base unit
    val verticalXLarge: Dp = 32.dp,   // 4 x base unit
    val verticalXXLarge: Dp = 40.dp,  // 5 x base unit
    val verticalXXXLarge: Dp = 48.dp, // 6 x base unit
    val verticalHuge: Dp = 56.dp,     // 7 x base unit
    val verticalXHuge: Dp = 64.dp,    // 8 x base unit

    /**
     * Horizontal dimensions
     */
    val horizontalXXSmall: Dp = 4.dp,   // 0.5 x base unit
    val horizontalXSmall: Dp = 8.dp,    // 1 x base unit
    val horizontalSmall: Dp = 12.dp,    // 1.5 x base unit
    val horizontalMedium: Dp = 16.dp,   // 2 x base unit
    val horizontalLarge: Dp = 24.dp,    // 3 x base unit
    val horizontalXLarge: Dp = 32.dp,   // 4 x base unit
    val horizontalXXLarge: Dp = 40.dp,  // 5 x base unit
    val horizontalXXXLarge: Dp = 48.dp, // 6 x base unit
    val horizontalHuge: Dp = 56.dp,     // 7 x base unit
    val horizontalXHuge: Dp = 64.dp,    // 8 x base unit

    /**
     * Other dimensions
     */
    val circularStrokeWith: Dp = 3.5.dp,
    val tabIndicatorHeight: Dp = 3.dp,
    val avatarBorderWidth: Dp = 3.dp,
    val linearProgressIndicatorHeight: Dp = 4.dp,
    val listingGridSpacing: Dp = 8.dp,
    val bottomNavFabElevation: Dp = 8.dp,
    val defaultCornerRadius: Dp = 12.dp,
    val bottomNavElevation: Dp = 12.dp,
    val defaultPadding: Dp = 16.dp,
    val iconDefaultSize: Dp = 24.dp,
    val bottomNavFabOffset: Dp = 28.dp,
    val tabIndicatorWidth: Dp = 32.dp,
    val settingsEditBadgeSize: Dp = 36.dp,
    val categoryChipHeight: Dp = 36.dp,
    val bottomNavFabInnerSize: Dp = 36.dp,
    val chatHeaderAvatarSize: Dp = 40.dp,
    val iconButtonDefaultSize: Dp = 40.dp,
    val searchBarHeight: Dp = 48.dp,
    val chatThreadAvatarSize: Dp = 48.dp,
    val chatInputHeight: Dp = 48.dp,
    val fieldsDefaultHeight: Dp = 50.dp,
    val bottomNavFabSize: Dp = 56.dp,
    val chatListingImageSize: Dp = 56.dp,
    val listingsHeaderCollapseDistance: Dp = 180.dp,
    val bottomNavHeight: Dp = 64.dp,
    val bottomNavFabOuterSize: Dp = 64.dp,
    val avatarSize: Dp = 80.dp,
    val settingsAvatarSize: Dp = 120.dp,
    val logoSize: Dp = 160.dp,
    val listingsLogoSize: Dp = 132.dp,
)

val LocalDimensions = compositionLocalOf { Dimensions() }
