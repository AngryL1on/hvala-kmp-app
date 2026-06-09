package tech.appard.hvala.shared.feature.listings.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.i18n.ListingsStrings
import tech.appard.hvala.shared.core.i18n.availabilityOptions
import tech.appard.hvala.shared.core.i18n.bodyTypeOptions
import tech.appard.hvala.shared.core.i18n.conditionOptions
import tech.appard.hvala.shared.core.i18n.drivetrainOptions
import tech.appard.hvala.shared.core.i18n.steeringWheelOptions
import tech.appard.hvala.shared.core.i18n.strings
import tech.appard.hvala.shared.core.i18n.transmissionOptions
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.model.PickedMedia
import tech.appard.hvala.shared.feature.listings.domain.GetCatalogDefaultsUseCase
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toCategoriesUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toRegionsByCountryUi
import tech.appard.hvala.shared.feature.listings.presentation.mapper.toLocationsUi
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCurrency
import tech.appard.hvala.shared.feature.listings.presentation.model.UILocationOption
import tech.appard.hvala.shared.feature.settings.domain.repository.LocaleRepository
import kotlin.time.Duration.Companion.milliseconds

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

data class CreateListingSelectOptions(
    val availability: List<SelectOption>,
    val bodyType: List<SelectOption>,
    val transmission: List<SelectOption>,
    val drivetrain: List<SelectOption>,
    val steeringWheel: List<SelectOption>,
    val condition: List<SelectOption>,
)

fun createListingSelectOptions(strings: ListingsStrings): CreateListingSelectOptions =
    CreateListingSelectOptions(
        availability = strings.availabilityOptions().map { SelectOption(it.id, it.label) },
        bodyType = strings.bodyTypeOptions().map { SelectOption(it.id, it.label) },
        transmission = strings.transmissionOptions().map { SelectOption(it.id, it.label) },
        drivetrain = strings.drivetrainOptions().map { SelectOption(it.id, it.label) },
        steeringWheel = strings.steeringWheelOptions().map { SelectOption(it.id, it.label) },
        condition = strings.conditionOptions().map { SelectOption(it.id, it.label) },
    )

class CreateListingViewModel(
    private val getCatalogDefaultsUseCase: GetCatalogDefaultsUseCase,
    private val localeRepository: LocaleRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow(CreateListingUiState())
    val state: StateFlow<CreateListingUiState> = _state.asStateFlow()

    init {
        scope.launch {
            localeRepository.languageFlow.collectLatest { language ->
                getCatalogDefaultsUseCase()
                _state.update {
                    it.copy(
                        categories = getCatalogDefaultsUseCase.categories().toCategoriesUi(language),
                        countries = getCatalogDefaultsUseCase.countries().toLocationsUi(language),
                        regionsByCountry = getCatalogDefaultsUseCase.regionsByCountry()
                            .toRegionsByCountryUi(language),
                    )
                }
            }
        }
    }

    fun load() {
        if (_state.value.categories.isNotEmpty()) return
        scope.launch {
            val language = localeRepository.getLanguage()
            getCatalogDefaultsUseCase()
            _state.update {
                it.copy(
                    categories = getCatalogDefaultsUseCase.categories().toCategoriesUi(language),
                    countries = getCatalogDefaultsUseCase.countries().toLocationsUi(language),
                    regionsByCountry = getCatalogDefaultsUseCase.regionsByCountry()
                        .toRegionsByCountryUi(language),
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
            delay(600.milliseconds)
            _state.update { it.copy(isSubmitting = false) }
            onSuccess()
        }
    }

    private fun updateField(block: (CreateListingUiState) -> CreateListingUiState) {
        _state.update(block)
    }

    private fun validate(state: CreateListingUiState): String? {
        val strings = localeRepository.getLanguage().strings().listings
        return when {
            state.title.isBlank() -> strings.errorTitleRequired
            state.phone.filter(Char::isDigit).length < 10 -> strings.errorPhoneInvalid
            state.countryId == null -> strings.errorCountryRequired
            state.regionId == null -> strings.errorRegionRequired
            state.location.isBlank() -> strings.errorLocationRequired
            state.price.isBlank() -> strings.errorPriceRequired
            state.categoryId == null -> strings.errorCategoryRequired
            state.photos.isEmpty() -> strings.errorPhotoRequired
            state.isAutoCategory && state.bodyTypeId == null -> strings.errorBodyTypeRequired
            state.isAutoCategory && state.transmissionId == null -> strings.errorTransmissionRequired
            else -> null
        }
    }

    companion object {
        const val AUTO_CATEGORY_ID = DEFAULT_AUTO_CATEGORY_ID
        const val AVAILABILITY_AVAILABLE = DEFAULT_AVAILABILITY_ID
        const val MAX_PHOTOS = 8
    }
}
