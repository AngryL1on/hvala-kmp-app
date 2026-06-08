package tech.appard.hvala.shared.feature.favorites.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingSortOrder

@Composable
fun FavoritesToolbar(
    sortOrder: UIListingSortOrder,
    onSortOrderChange: (UIListingSortOrder) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val sortOptions = UIListingSortOrder.entries.map {
        SelectOption(id = it.name, label = it.title)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(White)
            .padding(
                horizontal = dimensions.horizontalMedium,
                vertical = dimensions.verticalMedium,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = "Сортировка",
            style = FieldTitle.copy(color = GrayText),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HvalaSelectField(
                modifier = Modifier.weight(1f),
                label = "",
                options = sortOptions,
                selectedOptionId = sortOrder.name,
                onOptionSelected = { id ->
                    onSortOrderChange(UIListingSortOrder.valueOf(id))
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
}
