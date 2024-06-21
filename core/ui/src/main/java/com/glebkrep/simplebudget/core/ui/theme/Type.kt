package com.glebkrep.simplebudget.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.glebkrep.simplebudget.core.ui.R


private val interFontFamily = FontFamily(
    Font(
        R.font.inter,
    )
)
private val defaultTypography = Typography()

val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),
    displayMedium = defaultTypography.displayMedium.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),
    displaySmall = defaultTypography.displaySmall.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = interFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = interFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = interFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium
    ),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = interFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = interFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = interFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = interFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = interFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = interFontFamily)
)
