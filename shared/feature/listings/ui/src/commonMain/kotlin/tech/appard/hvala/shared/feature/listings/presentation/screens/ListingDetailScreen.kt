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
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailStateHolder
import tech.appard.hvala.shared.feature.listings.presentation.viewmodels.ListingDetailUiState
import tech.appard.hvala.shared.feature.listings.presentation.components.CreateListingMapPlaceholder
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingDetailInfoCard
import tech.appard.hvala.shared.feature.listings.presentation.components.ListingDetailPhotoPager

@Composable
fun ListingDetailScreen(
    listingId: String,
    stateHolder: ListingDetailStateHolder,
    modifier: Modifier = Modifier,
    isFavoriteOverride: Boolean? = null,
    onFavoriteToggle: (String) -> Unit = {},
    onContactClick: () -> Unit = {},
    onSellerClick: (String) -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()

    LaunchedEffect(listingId, isFavoriteOverride) {
        stateHolder.load(listingId, isFavoriteOverride)
    }

    ListingDetailContent(
        modifier = modifier,
        state = state,
        onPhotoSelected = stateHolder::onPhotoSelected,
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
                    text = state.error ?: "Listing not found",
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
                        title = "Details",
                        rows = buildDetailsRows(state),
                    )

                    CreateListingMapPlaceholder()

                    listing.autoDetails?.let { autoDetails ->
                        ListingDetailInfoCard(
                            title = "Vehicle details",
                            rows = buildAutoDetailsRows(autoDetails),
                        )
                    }

                    DescriptionSection(description = listing.description)

                    PrimaryButton(
                        text = "Contact seller",
                        onClick = onContactClick,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = ButtonLarge,
                    )

                    Spacer(modifier = Modifier.height(dimensions.verticalMedium))
                }
            }
        }
    }
}

@Composable
private fun SellerLinkRow(
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
                text = "Seller",
                style = FieldCaption.copy(color = GrayText),
            )
            Text(
                text = sellerName,
                style = LinkMedium.copy(color = SecondaryMain),
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Open seller profile",
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
            text = "Description",
            style = FieldCaption.copy(color = GrayText),
        )
        Text(
            text = description,
            style = BodyMedium.copy(color = InputText),
        )
    }
}

private fun buildDetailsRows(state: ListingDetailUiState): List<Pair<String, String>> {
    val listing = state.listing ?: return emptyList()
    return listOf(
        "Category" to state.categoryTitle,
        "Phone" to listing.phone,
        "Country" to state.countryTitle,
        "Region" to state.regionTitle,
        "Location" to listing.location,
    )
}

private fun buildAutoDetailsRows(details: UIListingAutoDetails): List<Pair<String, String>> = listOf(
    "Body type" to details.bodyType,
    "Color" to details.color,
    "Transmission" to details.transmission,
    "Drivetrain" to details.drivetrain,
    "Steering wheel" to details.steeringWheel,
    "Condition" to details.condition,
    "Number of owners" to details.numberOfOwners,
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
