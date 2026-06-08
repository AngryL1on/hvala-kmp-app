package tech.appard.hvala.shared.feature.listings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.core.contracts.model.ListingCategory
import tech.appard.hvala.shared.core.contracts.model.ListingCurrency
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.fields.HvalaSelectField
import tech.appard.hvala.shared.core.ui.components.fields.PhoneTextField
import tech.appard.hvala.shared.core.ui.components.fields.PrimaryTextField
import tech.appard.hvala.shared.core.ui.components.fields.SelectOption
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.Error
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.contracts.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.platform.rememberMediaPickerLauncher
import tech.appard.hvala.shared.feature.listings.components.CreateListingMapPlaceholder
import tech.appard.hvala.shared.feature.listings.components.CreateListingPhotoSection

@Composable
fun CreateListingScreen(
    stateHolder: CreateListingStateHolder,
    modifier: Modifier = Modifier,
    onSubmitted: () -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()
    val photoPicker = rememberMediaPickerLauncher(
        mode = MediaPickerMode.Images,
        onResult = stateHolder::onPhotosPicked,
    )

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    CreateListingContent(
        modifier = modifier,
        state = state,
        onTitleChange = stateHolder::onTitleChange,
        onPhoneChange = stateHolder::onPhoneChange,
        onCountryChange = stateHolder::onCountryChange,
        onRegionChange = stateHolder::onRegionChange,
        onLocationChange = stateHolder::onLocationChange,
        onPriceChange = stateHolder::onPriceChange,
        onCurrencyChange = stateHolder::onCurrencyChange,
        onAvailabilityChange = stateHolder::onAvailabilityChange,
        onCategoryChange = stateHolder::onCategoryChange,
        onBodyTypeChange = stateHolder::onBodyTypeChange,
        onColorChange = stateHolder::onColorChange,
        onTransmissionChange = stateHolder::onTransmissionChange,
        onDrivetrainChange = stateHolder::onDrivetrainChange,
        onSteeringWheelChange = stateHolder::onSteeringWheelChange,
        onConditionChange = stateHolder::onConditionChange,
        onNumberOfOwnersChange = stateHolder::onNumberOfOwnersChange,
        onDescriptionChange = stateHolder::onDescriptionChange,
        onAddPhotoClick = {
            val remaining = CreateListingStateHolder.MAX_PHOTOS - state.photos.size
            if (remaining > 0) {
                photoPicker.launch(remaining)
            }
        },
        onSubmit = { stateHolder.submit(onSubmitted) },
    )
}

@Composable
private fun CreateListingContent(
    state: CreateListingUiState,
    onTitleChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onCurrencyChange: (ListingCurrency) -> Unit,
    onAvailabilityChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onBodyTypeChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onTransmissionChange: (String) -> Unit,
    onDrivetrainChange: (String) -> Unit,
    onSteeringWheelChange: (String) -> Unit,
    onConditionChange: (String) -> Unit,
    onNumberOfOwnersChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddPhotoClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(dimensions.fieldsDefaultHeight)

    val categoryOptions = state.categories.map { SelectOption(it.id, it.title) }
    val countryOptions = state.countries.map { SelectOption(it.id, it.title) }
    val regionOptions = state.availableRegions.map { SelectOption(it.id, it.title) }
    val currencyOptions = ListingCurrency.entries.map { SelectOption(it.name, it.code) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(
                horizontal = dimensions.horizontalMedium,
                vertical = dimensions.verticalMedium,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
    ) {
        CreateListingPhotoSection(
            photos = state.photos,
            onAddPhotoClick = onAddPhotoClick,
            canAddMore = state.photos.size < CreateListingStateHolder.MAX_PHOTOS,
        )

        SectionTitle("Fill in description")

        FormField(label = "Title") {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.title,
                placeholder = "Listing title",
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onTitleChange,
            )
        }

        FormField(label = "Phone") {
            PhoneTextField(
                modifier = fieldModifier,
                value = state.phone,
                placeholder = "+7 (999) 000-00-00",
                isMaxQuantityOfCharVisible = false,
                onTextChange = onPhoneChange,
            )
        }

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = "Select country",
            options = countryOptions,
            selectedOptionId = state.countryId,
            placeholder = "Country",
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onCountryChange,
        )

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = "Select region",
            options = regionOptions,
            selectedOptionId = state.regionId,
            placeholder = "Region",
            enabled = state.countryId != null && regionOptions.isNotEmpty(),
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onRegionChange,
        )

        FormField(label = "Location") {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.location,
                placeholder = "Address",
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onLocationChange,
            )
        }

        CreateListingMapPlaceholder()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
        ) {
            FormField(
                label = "Price",
                modifier = Modifier.weight(1f),
            ) {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.price,
                    placeholder = "0",
                    isOnlyNumbers = true,
                    isMaxQuantityOfCharVisible = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    onTextChange = onPriceChange,
                )
            }

            HvalaSelectField(
                modifier = Modifier.weight(1f),
                label = "Select currency",
                options = currencyOptions,
                selectedOptionId = state.currency.name,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = { id ->
                    onCurrencyChange(ListingCurrency.valueOf(id))
                },
            )
        }

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = "Select availability",
            options = CreateListingStateHolder.availabilityOptions,
            selectedOptionId = state.availabilityId,
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onAvailabilityChange,
        )

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = "Select category",
            options = categoryOptions,
            selectedOptionId = state.categoryId,
            placeholder = "Category",
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onCategoryChange,
        )

        if (state.isAutoCategory) {
            SectionTitle("Vehicle details")

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = "Body type",
                options = CreateListingStateHolder.bodyTypeOptions,
                selectedOptionId = state.bodyTypeId,
                placeholder = "Body type",
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onBodyTypeChange,
            )

            FormField(label = "Color") {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.color,
                    placeholder = "Color",
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = onColorChange,
                )
            }

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = "Transmission",
                options = CreateListingStateHolder.transmissionOptions,
                selectedOptionId = state.transmissionId,
                placeholder = "Transmission",
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onTransmissionChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = "Drivetrain",
                options = CreateListingStateHolder.drivetrainOptions,
                selectedOptionId = state.drivetrainId,
                placeholder = "Drivetrain",
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onDrivetrainChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = "Steering wheel",
                options = CreateListingStateHolder.steeringWheelOptions,
                selectedOptionId = state.steeringWheelId,
                placeholder = "Steering wheel",
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onSteeringWheelChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = "Condition",
                options = CreateListingStateHolder.conditionOptions,
                selectedOptionId = state.conditionId,
                placeholder = "Condition",
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onConditionChange,
            )

            FormField(label = "Number of owners") {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.numberOfOwners,
                    placeholder = "1",
                    isOnlyNumbers = true,
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = onNumberOfOwnersChange,
                )
            }
        }

        FormField(label = "Description") {
            PrimaryTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.verticalXXXLarge * 2),
                value = state.description,
                placeholder = "Describe your listing",
                minLines = 4,
                maxLines = 6,
                singleLine = false,
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                onTextChange = onDescriptionChange,
            )
        }

        if (state.error != null) {
            Text(
                text = state.error,
                style = FieldCaption.copy(color = Error),
            )
        }

        PrimaryButton(
            text = "Submit",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSubmitting,
            loading = state.isSubmitting,
            textStyle = ButtonLarge,
        )

        Spacer(modifier = Modifier.height(dimensions.verticalMedium))
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = FieldTitle.copy(color = InputText),
        modifier = modifier,
    )
}

@Composable
private fun FormField(
    label: String,
    modifier: Modifier = Modifier,
    field: @Composable () -> Unit,
) {
    val dimensions = LocalDimensions.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
    ) {
        Text(
            text = label,
            style = FieldTitle.copy(color = InputText),
        )
        field()
    }
}

@Composable
@Preview
private fun CreateListingScreenPreview() {
    HvalaTheme {
        CreateListingContent(
            state = CreateListingUiState(
                categories = listOf(
                    ListingCategory(id = "auto", title = "Auto"),
                    ListingCategory(id = "clothes", title = "Clothes"),
                ),
                categoryId = "auto",
            ),
            onTitleChange = {},
            onPhoneChange = {},
            onCountryChange = {},
            onRegionChange = {},
            onLocationChange = {},
            onPriceChange = {},
            onCurrencyChange = {},
            onAvailabilityChange = {},
            onCategoryChange = {},
            onBodyTypeChange = {},
            onColorChange = {},
            onTransmissionChange = {},
            onDrivetrainChange = {},
            onSteeringWheelChange = {},
            onConditionChange = {},
            onNumberOfOwnersChange = {},
            onDescriptionChange = {},
            onAddPhotoClick = {},
            onSubmit = {},
        )
    }
}
