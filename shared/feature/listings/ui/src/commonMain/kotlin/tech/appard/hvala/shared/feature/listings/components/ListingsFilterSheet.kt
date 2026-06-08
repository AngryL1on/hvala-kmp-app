package tech.appard.hvala.shared.feature.listings.components

import androidx.compose.runtime.Composable
import tech.appard.hvala.shared.feature.listings.ui.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.ui.model.UIListingsFilters
import tech.appard.hvala.shared.feature.listings.ui.model.UILocationOption

@Composable
fun ListingsFilterSheet(
    visible: Boolean,
    draftFilters: UIListingsFilters,
    categories: List<UIListingCategory>,
    countries: List<UILocationOption>,
    regions: List<UILocationOption>,
    onDismiss: () -> Unit,
    onDraftChange: (UIListingsFilters) -> Unit,
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
