package tech.appard.hvala.shared.feature.listings.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain

@Composable
fun ListingsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
) {
    val dimensions = LocalDimensions.current

    PrimaryTextField(
        modifier = modifier,
        value = query,
        onTextChange = onQueryChange,
        placeholder = "Search",
        fieldMinHeight = dimensions.searchBarHeight,
        contentPadding = PaddingValues(
            horizontal = dimensions.horizontalXSmall,
            vertical = dimensions.verticalXSmall,
        ),
        leadingContent = {
            IconButton(
                onClick = onFilterClick,
                modifier = Modifier.size(dimensions.iconButtonDefaultSize),
            ) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "Фильтр",
                    tint = PrimaryMain,
                    modifier = Modifier.size(dimensions.iconDefaultSize),
                )
            }
        },
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
