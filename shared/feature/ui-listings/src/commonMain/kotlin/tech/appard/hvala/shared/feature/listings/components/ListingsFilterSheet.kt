package tech.appard.hvala.shared.feature.listings.components

import androidx.compose.runtime.Composable
import tech.appard.hvala.shared.core.contracts.model.ListingCategory
import tech.appard.hvala.shared.core.contracts.model.ListingsFilters
import tech.appard.hvala.shared.core.contracts.model.LocationOption
import tech.appard.hvala.shared.core.ui.components.filters.FilterSettingsSheet

@Composable
fun ListingsFilterSheet(
    visible: Boolean,
    draftFilters: ListingsFilters,
    categories: List<ListingCategory>,
    countries: List<LocationOption>,
    regions: List<LocationOption>,
    onDismiss: () -> Unit,
    onDraftChange: (ListingsFilters) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
) {
    FilterSettingsSheet(
        visible = visible,
        draftFilters = draftFilters,
        categories = categories,
        countries = countries,
        regions = regions,
        onDismiss = onDismiss,
        onDraftChange = onDraftChange,
        onReset = onReset,
        onApply = onApply,
    )
}
