package tech.appard.hvala.shared.feature.favorites.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.contracts.model.ListingSortOrder
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.White

@Composable
fun FavoritesToolbar(
    sortOrder: ListingSortOrder,
    onSortOrderChange: (ListingSortOrder) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val sortOptions = ListingSortOrder.entries.map {
        SelectOption(id = it.name, label = it.title)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                horizontal = dimensions.horizontalMedium,
                vertical = dimensions.verticalMedium,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HvalaSelectField(
            modifier = Modifier.weight(1f),
            label = "Сортировка",
            options = sortOptions,
            selectedOptionId = sortOrder.name,
            onOptionSelected = { id ->
                onSortOrderChange(ListingSortOrder.valueOf(id))
            },
            fieldMinHeight = dimensions.searchBarHeight,
        )

        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .padding(start = dimensions.horizontalXSmall)
                .size(dimensions.iconButtonDefaultSize),
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "Фильтры",
                tint = PrimaryMain,
                modifier = Modifier.size(dimensions.iconDefaultSize),
            )
        }
    }
}
