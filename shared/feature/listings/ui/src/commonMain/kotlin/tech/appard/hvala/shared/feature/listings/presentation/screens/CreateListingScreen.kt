package tech.appard.hvala.shared.feature.listings.presentation.screens

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
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.dialogs.HvalaConfirmDialog
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
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCategory
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingCurrency
import tech.appard.hvala.shared.core.ui.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.platform.rememberMediaPickerLauncher
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.listings.presentation.CreateListingViewModel
import tech.appard.hvala.shared.feature.listings.presentation.createListingSelectOptions
import tech.appard.hvala.shared.feature.listings.presentation.CreateListingUiState
import tech.appard.hvala.shared.feature.listings.presentation.components.CreateListingMapPlaceholder
import tech.appard.hvala.shared.feature.listings.presentation.components.CreateListingPhotoSection

@Composable
fun CreateListingScreen(
    viewModel: CreateListingViewModel,
    modifier: Modifier = Modifier,
    onSubmitted: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val strings = appStrings().listings
    val photoPicker = rememberMediaPickerLauncher(
        mode = MediaPickerMode.Images,
        onResult = viewModel::onPhotosPicked,
    )

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    CreateListingContent(
        modifier = modifier,
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onPhoneChange = viewModel::onPhoneChange,
        onCountryChange = viewModel::onCountryChange,
        onRegionChange = viewModel::onRegionChange,
        onLocationChange = viewModel::onLocationChange,
        onPriceChange = viewModel::onPriceChange,
        onCurrencyChange = viewModel::onCurrencyChange,
        onAvailabilityChange = viewModel::onAvailabilityChange,
        onCategoryChange = viewModel::onCategoryChange,
        onBodyTypeChange = viewModel::onBodyTypeChange,
        onColorChange = viewModel::onColorChange,
        onTransmissionChange = viewModel::onTransmissionChange,
        onDrivetrainChange = viewModel::onDrivetrainChange,
        onSteeringWheelChange = viewModel::onSteeringWheelChange,
        onConditionChange = viewModel::onConditionChange,
        onNumberOfOwnersChange = viewModel::onNumberOfOwnersChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddPhotoClick = {
            val remaining = CreateListingViewModel.MAX_PHOTOS - state.photos.size
            if (remaining > 0) {
                photoPicker.launch(remaining)
            }
        },
        onPhotoRemove = viewModel::onPhotoRemove,
        onSubmit = { viewModel.submit(onSubmitted) },
    )

    if (state.showExitConfirmation) {
        HvalaConfirmDialog(
            title = strings.exitConfirmTitle,
            message = strings.exitConfirmMessage,
            confirmText = strings.exit,
            isDestructive = true,
            onConfirm = viewModel::confirmExit,
            onDismiss = viewModel::dismissExitConfirmation,
        )
    }
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
    onCurrencyChange: (UIListingCurrency) -> Unit,
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
    onPhotoRemove: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().listings
    val selectOptions = createListingSelectOptions(strings)
    val fieldModifier = Modifier
        .fillMaxWidth()
        .height(dimensions.fieldsDefaultHeight)

    val categoryOptions = state.categories.map { SelectOption(it.id, it.title) }
    val countryOptions = state.countries.map { SelectOption(it.id, it.title) }
    val regionOptions = state.availableRegions.map { SelectOption(it.id, it.title) }
    val currencyOptions = UIListingCurrency.entries.map { SelectOption(it.name, it.code) }

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
            canAddMore = state.photos.size < CreateListingViewModel.MAX_PHOTOS,
            onPhotoRemove = onPhotoRemove,
        )

        SectionTitle(strings.fillDescription)

        FormField(label = strings.title) {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.title,
                placeholder = strings.listingTitle,
                isMaxQuantityOfCharVisible = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                onTextChange = onTitleChange,
            )
        }

        FormField(label = strings.phone) {
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
            label = strings.selectCountry,
            options = countryOptions,
            selectedOptionId = state.countryId,
            placeholder = strings.country,
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onCountryChange,
        )

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = strings.selectRegion,
            options = regionOptions,
            selectedOptionId = state.regionId,
            placeholder = strings.region,
            enabled = state.countryId != null && regionOptions.isNotEmpty(),
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onRegionChange,
        )

        FormField(label = strings.location) {
            PrimaryTextField(
                modifier = fieldModifier,
                value = state.location,
                placeholder = strings.location,
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
                label = strings.price,
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
                label = strings.selectCurrency,
                options = currencyOptions,
                selectedOptionId = state.currency.name,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = { id ->
                    onCurrencyChange(UIListingCurrency.valueOf(id))
                },
            )
        }

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = strings.availability,
            options = selectOptions.availability,
            selectedOptionId = state.availabilityId,
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onAvailabilityChange,
        )

        HvalaSelectField(
            modifier = Modifier.fillMaxWidth(),
            label = strings.selectCategory,
            options = categoryOptions,
            selectedOptionId = state.categoryId,
            placeholder = strings.category,
            fieldMinHeight = dimensions.fieldsDefaultHeight,
            onOptionSelected = onCategoryChange,
        )

        if (state.isAutoCategory) {
            SectionTitle(strings.vehicleDetails)

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = strings.bodyType,
                options = selectOptions.bodyType,
                selectedOptionId = state.bodyTypeId,
                placeholder = strings.bodyType,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onBodyTypeChange,
            )

            FormField(label = strings.color) {
                PrimaryTextField(
                    modifier = fieldModifier,
                    value = state.color,
                    placeholder = strings.color,
                    isMaxQuantityOfCharVisible = false,
                    onTextChange = onColorChange,
                )
            }

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = strings.transmission,
                options = selectOptions.transmission,
                selectedOptionId = state.transmissionId,
                placeholder = strings.transmission,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onTransmissionChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = strings.drivetrain,
                options = selectOptions.drivetrain,
                selectedOptionId = state.drivetrainId,
                placeholder = strings.drivetrain,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onDrivetrainChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = strings.steeringWheel,
                options = selectOptions.steeringWheel,
                selectedOptionId = state.steeringWheelId,
                placeholder = strings.steeringWheel,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onSteeringWheelChange,
            )

            HvalaSelectField(
                modifier = Modifier.fillMaxWidth(),
                label = strings.condition,
                options = selectOptions.condition,
                selectedOptionId = state.conditionId,
                placeholder = strings.condition,
                fieldMinHeight = dimensions.fieldsDefaultHeight,
                onOptionSelected = onConditionChange,
            )

            FormField(label = strings.numberOfOwners) {
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

        SectionTitle(strings.additionalInfo)

        FormField(label = strings.description) {
            PrimaryTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensions.verticalXXXLarge * 2),
                value = state.description,
                placeholder = strings.description,
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
            text = strings.submit,
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
                    UIListingCategory(id = "auto", title = "Auto"),
                    UIListingCategory(id = "clothes", title = "Clothes"),
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
            onPhotoRemove = {},
            onSubmit = {},
        )
    }
}
