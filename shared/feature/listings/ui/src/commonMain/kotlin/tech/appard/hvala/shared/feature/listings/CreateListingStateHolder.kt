package tech.appard.hvala.shared.feature.listings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.ui.model.PickedMedia
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.feature.listings.ui.mapper.toCategoriesUi
import tech.appard.hvala.shared.feature.listings.ui.mapper.toLocationsUi
import tech.appard.hvala.shared.feature.listings.ui.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.ui.model.UIListingCurrency
import tech.appard.hvala.shared.feature.listings.ui.model.UILocationOption

private const val DEFAULT_AVAILABILITY_ID = "available"
private const val DEFAULT_AUTO_CATEGORY_ID = "auto"

data class CreateListingUiState(
    val title: String = "",
    val phone: String = "",
    val countryId: String? = null,
    val regionId: String? = null,
    val location: String = "18 Stjepana Mitrova Ljubiše, Budva, Montenegro",
    val price: String = "",
    val currency: UIListingCurrency = UIListingCurrency.USD,
    val availabilityId: String = DEFAULT_AVAILABILITY_ID,
    val categoryId: String? = null,
    val bodyTypeId: String? = null,
    val color: String = "",
    val transmissionId: String? = null,
    val drivetrainId: String? = null,
    val steeringWheelId: String? = null,
    val conditionId: String? = null,
    val numberOfOwners: String = "",
    val description: String = "",
    val photos: List<PickedMedia> = emptyList(),
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val categories: List<UIListingCategory> = emptyList(),
    val countries: List<UILocationOption> = emptyList(),
    val regionsByCountry: Map<String, List<UILocationOption>> = emptyMap(),
) {
    val isAutoCategory: Boolean
        get() = categoryId == DEFAULT_AUTO_CATEGORY_ID

    val availableRegions: List<UILocationOption>
        get() = countryId?.let { regionsByCountry[it] }.orEmpty()
}

class CreateListingStateHolder(
    private val getCatalogDefaultsUseCase: GetCatalogDefaultsUseCase,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(CreateListingUiState())
    val state: StateFlow<CreateListingUiState> = _state.asStateFlow()

    fun load() {
        if (_state.value.categories.isNotEmpty()) return
        scope.launch {
            getCatalogDefaultsUseCase()
            _state.update {
                it.copy(
                    categories = getCatalogDefaultsUseCase.categories().toCategoriesUi(),
                    countries = getCatalogDefaultsUseCase.countries().toLocationsUi(),
                    regionsByCountry = getCatalogDefaultsUseCase.regionsByCountry()
                        .mapValues { (_, regions) -> regions.toLocationsUi() },
                )
            }
        }
    }

    fun onTitleChange(value: String) = updateField { it.copy(title = value, error = null) }
    fun onPhoneChange(value: String) = updateField { it.copy(phone = value, error = null) }
    fun onCountryChange(countryId: String) = updateField {
        it.copy(countryId = countryId, regionId = null, error = null)
    }
    fun onRegionChange(regionId: String) = updateField { it.copy(regionId = regionId, error = null) }
    fun onLocationChange(value: String) = updateField { it.copy(location = value, error = null) }
    fun onPriceChange(value: String) = updateField { it.copy(price = value, error = null) }
    fun onCurrencyChange(currency: UIListingCurrency) = updateField { it.copy(currency = currency, error = null) }
    fun onAvailabilityChange(availabilityId: String) = updateField {
        it.copy(availabilityId = availabilityId, error = null)
    }
    fun onCategoryChange(categoryId: String) = updateField { it.copy(categoryId = categoryId, error = null) }
    fun onBodyTypeChange(bodyTypeId: String) = updateField { it.copy(bodyTypeId = bodyTypeId, error = null) }
    fun onColorChange(value: String) = updateField { it.copy(color = value, error = null) }
    fun onTransmissionChange(transmissionId: String) = updateField {
        it.copy(transmissionId = transmissionId, error = null)
    }
    fun onDrivetrainChange(drivetrainId: String) = updateField {
        it.copy(drivetrainId = drivetrainId, error = null)
    }
    fun onSteeringWheelChange(steeringWheelId: String) = updateField {
        it.copy(steeringWheelId = steeringWheelId, error = null)
    }
    fun onConditionChange(conditionId: String) = updateField {
        it.copy(conditionId = conditionId, error = null)
    }
    fun onNumberOfOwnersChange(value: String) = updateField {
        it.copy(numberOfOwners = value, error = null)
    }
    fun onDescriptionChange(value: String) = updateField { it.copy(description = value, error = null) }

    fun onPhotosPicked(items: List<PickedMedia>) {
        if (items.isEmpty()) return
        _state.update { current ->
            val remaining = MAX_PHOTOS - current.photos.size
            if (remaining <= 0) return@update current
            current.copy(
                photos = current.photos + items.take(remaining),
                error = null,
            )
        }
    }

    fun submit(onSuccess: () -> Unit) {
        val snapshot = _state.value
        if (snapshot.isSubmitting) return

        val validationError = validate(snapshot)
        if (validationError != null) {
            _state.update { it.copy(error = validationError) }
            return
        }

        scope.launch {
            _state.update { it.copy(isSubmitting = true, error = null) }
            delay(600)
            _state.update { it.copy(isSubmitting = false) }
            onSuccess()
        }
    }

    private fun updateField(block: (CreateListingUiState) -> CreateListingUiState) {
        _state.update(block)
    }

    private fun validate(state: CreateListingUiState): String? = when {
        state.title.isBlank() -> "Enter a title"
        state.phone.filter(Char::isDigit).length < 10 -> "Enter a valid phone number"
        state.countryId == null -> "Select a country"
        state.regionId == null -> "Select a region"
        state.location.isBlank() -> "Enter a location"
        state.price.isBlank() -> "Enter a price"
        state.categoryId == null -> "Select a category"
        state.photos.isEmpty() -> "Add at least one photo"
        state.isAutoCategory && state.bodyTypeId == null -> "Select a body type"
        state.isAutoCategory && state.transmissionId == null -> "Select a transmission"
        else -> null
    }

    companion object {
        const val AUTO_CATEGORY_ID = DEFAULT_AUTO_CATEGORY_ID
        const val AVAILABILITY_AVAILABLE = DEFAULT_AVAILABILITY_ID
        const val MAX_PHOTOS = 8

        val availabilityOptions: List<SelectOption> = listOf(
            SelectOption(id = "available", label = "Available"),
            SelectOption(id = "reserved", label = "Reserved"),
            SelectOption(id = "sold", label = "Sold"),
        )

        val bodyTypeOptions: List<SelectOption> = listOf(
            SelectOption(id = "sedan", label = "Sedan"),
            SelectOption(id = "suv", label = "SUV"),
            SelectOption(id = "hatchback", label = "Hatchback"),
            SelectOption(id = "coupe", label = "Coupe"),
            SelectOption(id = "wagon", label = "Wagon"),
        )

        val transmissionOptions: List<SelectOption> = listOf(
            SelectOption(id = "manual", label = "Manual"),
            SelectOption(id = "automatic", label = "Automatic"),
        )

        val drivetrainOptions: List<SelectOption> = listOf(
            SelectOption(id = "fwd", label = "FWD"),
            SelectOption(id = "rwd", label = "RWD"),
            SelectOption(id = "awd", label = "AWD"),
        )

        val steeringWheelOptions: List<SelectOption> = listOf(
            SelectOption(id = "left", label = "Left"),
            SelectOption(id = "right", label = "Right"),
        )

        val conditionOptions: List<SelectOption> = listOf(
            SelectOption(id = "new", label = "New"),
            SelectOption(id = "used", label = "Used"),
            SelectOption(id = "parts", label = "For parts"),
        )
    }
}
