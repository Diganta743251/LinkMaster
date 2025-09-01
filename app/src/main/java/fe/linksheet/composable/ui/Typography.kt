package fe.linksheet.composable.ui

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import fe.linksheet.R
import kotlin.reflect.KProperty0

//
//val GoogleSansText = FontFamily(
//    Font(R.font.google_sans_text_regular),
//    Font(R.font.google_sans_text_medium, FontWeight.Medium),
//    Font(R.font.google_sans_text_bold, FontWeight.Bold),
//)

val HkGroteskFontFamily = FontFamily(
//    Font(resId = R.font.hankengrotesk),
//    Font(resId = R.font.hankengrotesk, weight = FontWeight.Medium),
//    Font(resId = R.font.hankengrotesk, weight = FontWeight.SemiBold),
//    Font(resId = R.font.hankengrotesk, weight = FontWeight.Bold)
    Font(resId = R.font.hkgroteskregular, weight = FontWeight.Normal),
    Font(resId = R.font.hkgroteskmedium, weight = FontWeight.Medium),
    Font(resId = R.font.hkgrotesksemibold, weight = FontWeight.SemiBold),
    Font(resId = R.font.hkgroteskbold, weight = FontWeight.Bold)
)

val PoppinsFontFamily = FontFamily(
    Font(R.font.poppinslight, FontWeight.Thin),
    Font(R.font.poppinslight, FontWeight.Light),
    Font(R.font.poppinsregular, FontWeight.Normal),
    Font(R.font.poppinsmedium, FontWeight.Medium),
    Font(R.font.poppinssemibold, FontWeight.SemiBold),
    Font(R.font.poppinsbold, FontWeight.Bold)
)

val NewDefaultTypography = Typography()

val HkGroteskSemiBold = TextStyle(
    fontFamily = HkGroteskFontFamily,
    fontWeight = FontWeight.SemiBold
)

val DialogTitleStyle = TextStyle(
    fontFamily = HkGroteskFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
)

// ListItem fonts
// TypographyKeyTokens.BodyLarge
// TypographyKeyTokens.BodyMedium
//val test = Typography.fromToken()

// Alertdialog
// headlineSmall
// bodyMedium

// Set of Material typography styles to start with
//val TitleMedium = DefaultTextStyle.copy(
//    fontFamily = TypeScaleTokens.TitleMediumFont,
//    fontWeight = TypeScaleTokens.TitleMediumWeight,
//    fontSize = TypeScaleTokens.TitleMediumSize,
//    lineHeight = TypeScaleTokens.TitleMediumLineHeight,
//    letterSpacing = TypeScaleTokens.TitleMediumTracking,
//)

// TypefaceTokens
//val Plain = FontFamily.SansSerif
// TypeTokens
//val WeightMedium = FontWeight.Medium

//val TitleMediumFont = TypefaceTokens.Plain
//val TitleMediumLineHeight = 24.0.sp
//val TitleMediumSize = 16.sp
//val TitleMediumTracking = 0.2.sp
//val TitleMediumWeight = TypefaceTokens.WeightMedium
val NewTypography = Typography(
    // Display styles for hero content
    displayLarge = NewDefaultTypography.displayLarge.merge(HkGroteskSemiBold).copy(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = NewDefaultTypography.displayMedium.merge(HkGroteskSemiBold).copy(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = NewDefaultTypography.displaySmall.merge(HkGroteskSemiBold).copy(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // Headline styles for section headers
    headlineLarge = NewDefaultTypography.headlineLarge.merge(HkGroteskSemiBold).copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = NewDefaultTypography.headlineMedium.merge(HkGroteskSemiBold).copy(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = NewDefaultTypography.headlineSmall.merge(HkGroteskSemiBold).copy(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // Title styles for cards and components
    titleLarge = NewDefaultTypography.titleLarge.merge(HkGroteskSemiBold).copy(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = NewDefaultTypography.titleMedium.merge(HkGroteskSemiBold).copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = NewDefaultTypography.titleSmall.merge(HkGroteskSemiBold).copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body styles for content
    bodyLarge = NewDefaultTypography.bodyLarge.copy(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = NewDefaultTypography.bodyMedium.copy(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = NewDefaultTypography.bodySmall.copy(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Label styles for buttons and UI elements
    labelLarge = NewDefaultTypography.labelLarge.merge(HkGroteskSemiBold).copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = NewDefaultTypography.labelMedium.merge(HkGroteskSemiBold).copy(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = NewDefaultTypography.labelSmall.merge(HkGroteskSemiBold).copy(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// Modern 2025 Typography Extensions
val Modern2025Typography = Typography(
    // Enhanced display styles with better spacing
    displayLarge = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 72.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.25).sp
    ),
    
    // Modern headline styles
    headlineLarge = TextStyle(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    
    // Enhanced body text for better readability
    bodyLarge = TextStyle(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = HkGroteskFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    )
)

@Deprecated(message = "Should no longer be used in new composables")
val LegacyTypography = Typography(
    titleLarge = DialogTitleStyle
)

private data class TypographyPreviewState(
    val property: KProperty0<TextStyle>,
    val style: TextStyle = property.get(),
    val text: String = property.name,
)

private class SmallTypographyPreviewProvider() : PreviewParameterProvider<TypographyPreviewState> {
    override val values = sequenceOf(
        TypographyPreviewState(NewTypography::headlineSmall),
        TypographyPreviewState(NewTypography::titleSmall),
        TypographyPreviewState(NewTypography::bodySmall),
    )
}

private class MediumTypographyPreviewProvider() : PreviewParameterProvider<TypographyPreviewState> {
    override val values = sequenceOf(
        TypographyPreviewState(NewTypography::headlineMedium),
        TypographyPreviewState(NewTypography::titleMedium),
        TypographyPreviewState(NewTypography::bodyMedium),
    )
}

private class LargeTypographyPreviewProvider() : PreviewParameterProvider<TypographyPreviewState> {
    override val values = sequenceOf(
        TypographyPreviewState(NewTypography::headlineLarge),
        TypographyPreviewState(NewTypography::titleLarge),
        TypographyPreviewState(NewTypography::bodyLarge),
    )
}

@Preview(group = "Small", showBackground = true, widthDp = 600)
@Composable
private fun NewTypographyPreview_Small(@PreviewParameter(SmallTypographyPreviewProvider::class) state: TypographyPreviewState) {
    NewTypographyPreviewBase(state = state)
}

@Preview(group = "Medium", showBackground = true, widthDp = 600)
@Composable
private fun NewTypographyPreview_Medium(@PreviewParameter(MediumTypographyPreviewProvider::class) state: TypographyPreviewState) {
    NewTypographyPreviewBase(state = state)
}

@Preview(group = "Large", showBackground = true, widthDp = 600)
@Composable
private fun NewTypographyPreview_Large(@PreviewParameter(LargeTypographyPreviewProvider::class) state: TypographyPreviewState) {
    NewTypographyPreviewBase(state = state)
}

@Composable
private fun NewTypographyPreviewBase(state: TypographyPreviewState) {
    val size = with(state.style) {
        "(w=${fontWeight?.weight},s=$fontSize,lH=$lineHeight,s=$letterSpacing)"
    }

    Text(
        text = "${state.text} $size",
        style = state.style
    )
}
