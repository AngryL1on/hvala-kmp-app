package tech.appard.hvala.shared.feature.listings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.core.i18n.sortOrderTitle
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.buttons.SecondaryOutlineButton
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.PrimaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleLarge
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toSortOrderId
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCurrency
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingSortOrder
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingsFilters
import tech.appard.hvala.shared.feature.listings.presentation.model.UILocationOption

@Composable
fun FilterSettingsSheet(
    visible: Boolean,
    draftFilters: UIListingsFilters,
    categories: List<UIListingCategory>,
    countries: List<UILocationOption>,
    regions: List<UILocationOption>,
    onDismiss: () -> Unit,
    onDraftChange: (UIListingsFilters) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    title: String? = null,
    sortOrder: UIListingSortOrder? = null,
    onSortOrderChange: ((UIListingSortOrder) -> Unit)? = null,
) {
    if (!visible) return

    val strings = appStrings().listings
    val common = appStrings().common
    val sheetTitle = title ?: strings.filterTitle
    val dimensions = LocalDimensions.current
    val fieldHeight = dimensions.fieldsDefaultHeight
    val currencyOptions = UIListingCurrency.entries.map {
        SelectOption(id = it.name, label = it.code)
    }
    val categoryOptions = categories.map {
        SelectOption(id = it.id, label = it.title)
    }
    val countryOptions = countries.map {
        SelectOption(id = it.id, label = it.title)
    }
    val regionOptions = regions.map {
        SelectOption(id = it.id, label = it.title)
    }
    val sortOptions = UIListingSortOrder.entries.map {
        SelectOption(id = it.name, label = strings.sortOrderTitle(it.toSortOrderId()))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.horizontalMedium),
            shape = RoundedCornerShape(dimensions.defaultCornerRadius),
            color = White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(dimensions.horizontalMedium),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = sheetTitle,
                        style = TitleLarge.copy(color = PrimaryMain),
                        modifier = Modifier.align(Alignment.Center),
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterEnd),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = common.close,
                            tint = GrayText,
                        )
                    }
                }

                if (sortOrder != null && onSortOrderChange != null) {
                    HvalaSelectField(
                        modifier = Modifier.fillMaxWidth(),
                        label = strings.sortBy,
                        options = sortOptions,
                        selectedOptionId = sortOrder.name,
                        onOptionSelected = { id ->
                            onSortOrderChange(UIListingSortOrder.valueOf(id))
                        },
                        fieldMinHeight = fieldHeight,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
                    verticalAlignment = Alignment.Top,
                ) {
                    HvalaSelectField(
                        modifier = Modifier.weight(0.9f),
                        label = strings.selectCurrency,
                        options = currencyOptions,
                        selectedOptionId = draftFilters.currency.name,
                        onOptionSelected = { id ->
                            onDraftChange(
                                draftFilters.copy(
                                    currency = UIListingCurrency.valueOf(id),
                                ),
                            )
                        },
                        fieldMinHeight = fieldHeight,
                    )

                    Column(
                        modifier = Modifier.weight(1.1f),
                        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
                    ) {
                        Text(
                            text = strings.priceRange,
                            style = FieldTitle.copy(color = GrayText),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXSmall),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            PrimaryTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(fieldHeight),
                                value = draftFilters.minPrice,
                                placeholder = strings.min,
                                isOnlyNumbers = true,
                                isMaxQuantityOfCharVisible = false,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next,
                                ),
                                onTextChange = { value ->
                                    onDraftChange(draftFilters.copy(minPrice = value))
                                },
                            )
                            Text(
                                text = "–",
                                style = FieldTitle.copy(color = GrayText),
                            )
                            PrimaryTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(fieldHeight),
                                value = draftFilters.maxPrice,
                                placeholder = strings.max,
                                isOnlyNumbers = true,
                                isMaxQuantityOfCharVisible = false,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                ),
                                onTextChange = { value ->
                                    onDraftChange(draftFilters.copy(maxPrice = value))
                                },
                            )
                        }
                    }
                }

                HvalaSelectField(
                    modifier = Modifier.fillMaxWidth(),
                    label = strings.selectLocation,
                    options = countryOptions,
                    selectedOptionId = draftFilters.countryId,
                    placeholder = strings.country,
                    onOptionSelected = { countryId ->
                        onDraftChange(
                            draftFilters.copy(
                                countryId = countryId,
                                regionId = null,
                            ),
                        )
                    },
                    fieldMinHeight = fieldHeight,
                )

                HvalaSelectField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "",
                    options = regionOptions,
                    selectedOptionId = draftFilters.regionId,
                    placeholder = strings.region,
                    enabled = draftFilters.countryId != null && regionOptions.isNotEmpty(),
                    onOptionSelected = { regionId ->
                        onDraftChange(draftFilters.copy(regionId = regionId))
                    },
                    fieldMinHeight = fieldHeight,
                )

                HvalaSelectField(
                    modifier = Modifier.fillMaxWidth(),
                    label = strings.selectCategory,
                    options = categoryOptions,
                    selectedOptionId = draftFilters.categoryId,
                    placeholder = strings.category,
                    onOptionSelected = { categoryId ->
                        onDraftChange(draftFilters.copy(categoryId = categoryId))
                    },
                    fieldMinHeight = fieldHeight,
                )

                SecondaryOutlineButton(
                    text = strings.resetFilters,
                    onClick = onReset,
                    modifier = Modifier.fillMaxWidth(),
                )

                PrimaryButton(
                    text = strings.apply,
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensions.verticalSmall),
                    textStyle = ButtonLarge,
                )
            }
        }
    }
}
