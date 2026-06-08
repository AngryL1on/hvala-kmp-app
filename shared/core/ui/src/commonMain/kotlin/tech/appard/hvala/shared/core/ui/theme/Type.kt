package tech.appard.hvala.shared.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val DefaultFontFamily = FontFamily.Default

private fun hvalaTextStyle(
    fontWeight: FontWeight,
    fontSize: TextUnit,
    lineHeight: TextUnit,
    letterSpacing: TextUnit = 0.sp,
): TextStyle = TextStyle(
    fontFamily = DefaultFontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
)

/** App bar title — 22/28 Medium */
val TitleLarge = hvalaTextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 22.sp,
    lineHeight = 28.sp,
)

/** Field labels, section titles — 16/24 Medium */
val FieldTitle = hvalaTextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

/** Field input & body text — 16/24 Regular */
val FieldInput = hvalaTextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.5.sp,
)

/** Secondary body, footer hints — 14/20 Regular */
val BodyMedium = hvalaTextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.25.sp,
)

/** Links: forgot password, sign up — 14/20 Medium */
val LinkMedium = hvalaTextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
)

/** Default button label — 14/20 Medium */
val ButtonMedium = hvalaTextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    letterSpacing = 0.1.sp,
)

/** Large primary action button — 18/24 Bold */
val ButtonLarge = hvalaTextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
)

/** Captions, validation errors — 12/16 Medium */
val FieldCaption = hvalaTextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)

/** Profile name, listing price — 16/24 Bold */
val TitleMedium = hvalaTextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

val Typography = Typography(
    titleLarge = TitleLarge,
    titleMedium = FieldTitle,
    bodyLarge = FieldInput,
    bodyMedium = BodyMedium,
    bodySmall = FieldCaption,
    labelLarge = ButtonLarge,
    labelMedium = ButtonMedium,
)
