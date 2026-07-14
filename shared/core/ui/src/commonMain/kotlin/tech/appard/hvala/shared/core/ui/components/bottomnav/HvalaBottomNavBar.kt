package tech.appard.hvala.shared.core.ui.components.bottomnav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.GrayPlaceholder
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Immutable
private data class BottomNavTabConfig(
    val item: BottomNavItem,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

@Composable
fun HvalaBottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = appStrings().nav
    val tabs = remember(strings) {
        listOf(
            BottomNavTabConfig(
                item = BottomNavItem.Listings,
                label = strings.listings,
                icon = Icons.Outlined.ShoppingCart,
                selectedIcon = Icons.Filled.ShoppingCart,
            ),
            BottomNavTabConfig(
                item = BottomNavItem.Write,
                label = strings.write,
                icon = Icons.Outlined.ChatBubbleOutline,
                selectedIcon = Icons.Filled.ChatBubble,
            ),
            BottomNavTabConfig(
                item = BottomNavItem.Favorites,
                label = strings.favorites,
                icon = Icons.Outlined.StarBorder,
                selectedIcon = Icons.Filled.Star,
            ),
            BottomNavTabConfig(
                item = BottomNavItem.Profile,
                label = strings.profile,
                icon = Icons.Outlined.PersonOutline,
                selectedIcon = Icons.Filled.Person,
            ),
        )
    }
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
    ) {
        HorizontalDivider(color = CardBorder)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensions.horizontalXSmall,
                    vertical = dimensions.verticalXSmall,
                ),
            verticalAlignment = Alignment.Top,
        ) {
            BottomNavTab(
                config = tabs[0],
                isSelected = selectedItem == BottomNavItem.Listings,
                onClick = { onItemSelected(BottomNavItem.Listings) },
                modifier = Modifier.weight(1f),
            )
            BottomNavTab(
                config = tabs[1],
                isSelected = selectedItem == BottomNavItem.Write,
                onClick = { onItemSelected(BottomNavItem.Write) },
                modifier = Modifier.weight(1f),
            )
            BottomNavAddButton(
                addLabel = strings.add,
                onClick = onAddClick,
                modifier = Modifier.weight(1f),
            )
            BottomNavTab(
                config = tabs[2],
                isSelected = selectedItem == BottomNavItem.Favorites,
                onClick = { onItemSelected(BottomNavItem.Favorites) },
                modifier = Modifier.weight(1f),
            )
            BottomNavTab(
                config = tabs[3],
                isSelected = selectedItem == BottomNavItem.Profile,
                onClick = { onItemSelected(BottomNavItem.Profile) },
                modifier = Modifier.weight(1f),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .background(White),
        )
    }
}

@Composable
private fun BottomNavTab(
    config: BottomNavTabConfig,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val tint = if (isSelected) PrimaryMain else GrayPlaceholder

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Icon(
            imageVector = if (isSelected) config.selectedIcon else config.icon,
            contentDescription = config.label,
            tint = tint,
            modifier = Modifier.size(dimensions.iconDefaultSize),
        )
        Text(
            text = config.label,
            style = FieldCaption.copy(
                color = tint,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun BottomNavAddButton(
    addLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Box(
            modifier = Modifier.size(dimensions.iconDefaultSize),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.bottomNavAddSize)
                    .clip(CircleShape)
                    .background(SecondaryMain),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = addLabel,
                    tint = White,
                    modifier = Modifier.size(dimensions.bottomNavAddIconSize),
                )
            }
        }
        Text(
            text = addLabel,
            style = FieldCaption.copy(color = GrayPlaceholder),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
