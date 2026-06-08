package tech.appard.hvala.shared.feature.messages.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain

@Composable
fun MessagesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current

    PrimaryTextField(
        modifier = modifier,
        value = query,
        onTextChange = onQueryChange,
        placeholder = "Поиск по чатам",
        cornerRadius = dimensions.searchBarCornerRadius,
        fieldMinHeight = dimensions.searchBarHeight,
        contentPadding = PaddingValues(
            horizontal = dimensions.horizontalMedium,
            vertical = dimensions.verticalXSmall,
        ),
        trailingContent = {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Поиск",
                    tint = PrimaryMain,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
    )
}
