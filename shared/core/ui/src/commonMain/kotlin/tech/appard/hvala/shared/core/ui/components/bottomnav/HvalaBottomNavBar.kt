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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
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
)

private val sideTabs = listOf(
    BottomNavTabConfig(BottomNavItem.Listings, "Объявления", Icons.Outlined.ShoppingCart),
    BottomNavTabConfig(BottomNavItem.Write, "Написать", Icons.Outlined.ChatBubbleOutline),
    BottomNavTabConfig(BottomNavItem.Favorites, "Избранное", Icons.Outlined.StarBorder),
    BottomNavTabConfig(BottomNavItem.Profile, "Профиль", Icons.Outlined.PersonOutline),
)

@Composable
fun HvalaBottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val barShape = RoundedCornerShape(
        topStart = dimensions.bottomNavCornerRadius,
        topEnd = dimensions.bottomNavCornerRadius,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.bottomNavFabOffset),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = dimensions.bottomNavElevation,
                        shape = barShape,
                        clip = false,
                    ),
                shape = barShape,
                color = White,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensions.bottomNavHeight)
                        .padding(horizontal = dimensions.horizontalXSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BottomNavTab(
                        config = sideTabs[0],
                        isSelected = selectedItem == BottomNavItem.Listings,
                        onClick = { onItemSelected(BottomNavItem.Listings) },
                        modifier = Modifier.weight(1f),
                    )
                    BottomNavTab(
                        config = sideTabs[1],
                        isSelected = selectedItem == BottomNavItem.Write,
                        onClick = { onItemSelected(BottomNavItem.Write) },
                        modifier = Modifier.weight(1f),
                    )
                    BottomNavAddSlot(
                        onClick = onAddClick,
                        modifier = Modifier.weight(1f),
                    )
                    BottomNavTab(
                        config = sideTabs[2],
                        isSelected = selectedItem == BottomNavItem.Favorites,
                        onClick = { onItemSelected(BottomNavItem.Favorites) },
                        modifier = Modifier.weight(1f),
                    )
                    BottomNavTab(
                        config = sideTabs[3],
                        isSelected = selectedItem == BottomNavItem.Profile,
                        onClick = { onItemSelected(BottomNavItem.Profile) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            BottomNavAddButton(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = -dimensions.bottomNavFabOffset),
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
            )
            .padding(vertical = dimensions.verticalXSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Icon(
            imageVector = config.icon,
            contentDescription = config.label,
            tint = tint,
            modifier = Modifier.size(dimensions.iconDefaultSize),
        )
        Text(
            text = config.label,
            style = FieldCaption.copy(color = tint),
        )
    }
}

@Composable
private fun BottomNavAddSlot(
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
            )
            .padding(top = dimensions.bottomNavFabOffset + dimensions.verticalXXSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Добавить",
            style = FieldCaption.copy(color = GrayPlaceholder),
        )
    }
}

@Composable
private fun BottomNavAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current

    Box(
        modifier = modifier
            .size(dimensions.bottomNavFabOuterSize)
            .shadow(
                elevation = dimensions.bottomNavFabElevation,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .background(White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(dimensions.bottomNavFabSize)
                .clip(CircleShape)
                .background(SecondaryMain),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(dimensions.bottomNavFabInnerSize)
                    .clip(CircleShape)
                    .background(White),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Добавить",
                    tint = SecondaryMain,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        }
    }
}
