package tech.appard.hvala.shared.feature.listings.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListing
import tech.appard.hvala.shared.feature.listings.presentation.model.UIListingAutoDetails
import tech.appard.hvala.shared.core.ui.components.buttons.PrimaryButton
import tech.appard.hvala.shared.core.ui.components.buttons.SecondaryOutlineButton
import tech.appard.hvala.shared.core.ui.platform.rememberPhoneDialer
import tech.appard.hvala.shared.core.ui.theme.BodyMedium
import tech.appard.hvala.shared.core.ui.theme.ButtonLarge
import tech.appard.hvala.shared.core.ui.theme.FieldCaption
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.InputText
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.LinkMedium
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleLarge
import tech.appard.hvala.shared.core.ui.theme.TitleMedium
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.ui.theme.CardBorder
import tech.appard.hvala.shared.core.i18n.ListingsStrings
import tech.appard.hvala.shared.core.i18n.appStrings
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailViewModel
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailUiState
import tech.appard.hvala.shared.feature.listings.presentation.components.CreateListingMapPlaceholder
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingDetailInfoCard
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingDetailPhotoPager

@Composable
fun ListingDetailScreen(
    listingId: String,
    viewModel: ListingDetailViewModel,
    modifier: Modifier = Modifier,
    isFavoriteOverride: Boolean? = null,
    onFavoriteToggle: (String) -> Unit = {},
    onContactClick: () -> Unit = {},
    onSellerClick: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(listingId, isFavoriteOverride) {
        viewModel.load(listingId, isFavoriteOverride)
    }

    ListingDetailContent(
        modifier = modifier,
        state = state,
        onPhotoSelected = viewModel::onPhotoSelected,
        onFavoriteClick = {
            state.listing?.id?.let(onFavoriteToggle)
        },
        onContactClick = onContactClick,
        onSellerClick = onSellerClick,
    )
}

@Composable
private fun ListingDetailContent(
    state: ListingDetailUiState,
    onPhotoSelected: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onContactClick: () -> Unit,
    onSellerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val strings = appStrings().listings
    val phoneDialer = rememberPhoneDialer()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = SecondaryMain,
                )
            }
            state.listing == null -> {
                Text(
                    text = state.error ?: strings.listingNotFound,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(dimensions.horizontalMedium),
                    style = BodyMedium.copy(color = GrayText),
                    textAlign = TextAlign.Center,
                )
            }
            else -> {
                val listing = state.listing

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = dimensions.horizontalMedium,
                            vertical = dimensions.verticalMedium,
                        ),
                    verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
                ) {
                    ListingDetailPhotoPager(
                        listing = listing,
                        currentPhotoIndex = state.currentPhotoIndex,
                        onPhotoSelected = onPhotoSelected,
                        onFavoriteClick = onFavoriteClick,
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)) {
                        Text(
                            text = listing.title,
                            style = TitleLarge.copy(color = InputText),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalXXSmall),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${listing.priceUsd}$",
                                style = TitleMedium.copy(color = InputText),
                            )
                            Text(
                                text = "= ${formatPriceRub(listing.priceRub)} ₽",
                                style = FieldCaption.copy(color = GrayText),
                            )
                        }
                        Text(
                            text = listing.postedAt,
                            style = FieldCaption.copy(color = GrayText),
                        )
                        SellerLinkRow(
                            sellerLabel = strings.seller,
                            openProfileLabel = strings.openSellerProfile,
                            sellerName = listing.sellerName,
                            onClick = {
                                if (listing.sellerId.isNotBlank()) {
                                    onSellerClick(listing.sellerId)
                                }
                            },
                        )
                    }

                    AvailabilityBadge(availability = listing.availability)

                    ListingDetailInfoCard(
                        title = strings.details,
                        rows = buildDetailsRows(state, strings),
                    )

                    CreateListingMapPlaceholder()

                    listing.autoDetails?.let { autoDetails ->
                        ListingDetailInfoCard(
                            title = strings.vehicleDetails,
                            rows = buildAutoDetailsRows(autoDetails, strings),
                        )
                    }

                    DescriptionSection(
                        title = strings.description,
                        description = listing.description,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.horizontalSmall),
                    ) {
                        SecondaryOutlineButton(
                            text = strings.callSeller,
                            onClick = { phoneDialer.dial(listing.phone) },
                            modifier = Modifier.weight(1f),
                            enabled = listing.phone.isNotBlank(),
                            textStyle = ButtonLarge,
                        )
                        PrimaryButton(
                            text = strings.contactSeller,
                            onClick = onContactClick,
                            modifier = Modifier.weight(1f),
                            textStyle = ButtonLarge,
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensions.verticalMedium))
                }
            }
        }
    }
}

@Composable
private fun SellerLinkRow(
    sellerLabel: String,
    openProfileLabel: String,
    sellerName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = dimensions.verticalXXSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall)) {
            Text(
                text = sellerLabel,
                style = FieldCaption.copy(color = GrayText),
            )
            Text(
                text = sellerName,
                style = LinkMedium.copy(color = SecondaryMain),
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = openProfileLabel,
            tint = SecondaryMain,
        )
    }
}

@Composable
private fun AvailabilityBadge(
    availability: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Box(
        modifier = modifier
            .clip(shape)
            .background(SecondaryMain.copy(alpha = 0.15f))
            .padding(
                horizontal = dimensions.horizontalSmall,
                vertical = dimensions.verticalXXSmall,
            ),
    ) {
        Text(
            text = availability,
            style = FieldTitle.copy(color = SecondaryMain),
        )
    }
}

@Composable
private fun DescriptionSection(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val shape = RoundedCornerShape(dimensions.defaultCornerRadius)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(White)
            .border(width = 1.dp, color = CardBorder, shape = shape)
            .padding(dimensions.horizontalMedium),
        verticalArrangement = Arrangement.spacedBy(dimensions.verticalSmall),
    ) {
        Text(
            text = title,
            style = FieldCaption.copy(color = GrayText),
        )
        Text(
            text = description,
            style = BodyMedium.copy(color = InputText),
        )
    }
}

private fun buildDetailsRows(state: ListingDetailUiState, strings: ListingsStrings): List<Pair<String, String>> {
    val listing = state.listing ?: return emptyList()
    return listOf(
        strings.detailCategory to state.categoryTitle,
        strings.detailPhone to listing.phone,
        strings.detailCountry to state.countryTitle,
        strings.detailRegion to state.regionTitle,
        strings.detailLocation to listing.location,
    )
}

private fun buildAutoDetailsRows(details: UIListingAutoDetails, strings: ListingsStrings): List<Pair<String, String>> =
    listOf(
        strings.detailBodyType to details.bodyType,
        strings.detailColor to details.color,
        strings.detailTransmission to details.transmission,
        strings.detailDrivetrain to details.drivetrain,
        strings.detailSteeringWheel to details.steeringWheel,
        strings.detailCondition to details.condition,
        strings.detailOwners to details.numberOfOwners,
    )

private fun formatPriceRub(priceRub: Int): String =
    priceRub.toString().reversed().chunked(3).joinToString(" ").reversed()

@Composable
@Preview
private fun ListingDetailScreenPreview() {
    HvalaTheme {
        ListingDetailContent(
            state = ListingDetailUiState(
                listing = UIListing(
                    id = "listing-8",
                    title = "BMW X5",
                    priceUsd = 41_000,
                    priceRub = 3_430_000,
                    location = "Moscow",
                    categoryId = "auto",
                    totalImages = 6,
                    isFavorite = true,
                    phone = "+382 67 123 456",
                    description = "Excellent SUV with full service history.",
                    availability = "Available",
                    sellerId = "alex",
                    sellerName = "Alex M.",
                    postedAt = "2 days ago",
                    autoDetails = UIListingAutoDetails(
                        bodyType = "SUV",
                        color = "Black",
                        transmission = "Automatic",
                        drivetrain = "AWD",
                        steeringWheel = "Left",
                        condition = "Used",
                        numberOfOwners = "2",
                    ),
                ),
                categoryTitle = "Auto",
                countryTitle = "Russia",
                regionTitle = "Moscow",
            ),
            onPhotoSelected = {},
            onFavoriteClick = {},
            onContactClick = {},
            onSellerClick = {},
        )
    }
}
