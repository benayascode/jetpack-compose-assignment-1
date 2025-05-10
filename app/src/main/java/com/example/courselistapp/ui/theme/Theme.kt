package com.example.courselistapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val DarkChocolate = Color(0xFF3E2723) // Deep chocolate brown for dark mode background
val MilkChocolate = Color(0xFF6D4C41) // Medium brown for primary elements
val CreamyWhite = Color(0xFFEFDEDD) // Soft cream color for light mode background
val CocoaAccent = Color(0xFFA1887F) // Light cocoa accent for secondary elements
val DeepCocoaText = Color(0xFF563D2D) // Dark cocoa text for readability
val MilChocolate = Color(0xFEF3FEDE)
val DarChocolate = Color(0xFEEEEEEE)
val LightCocoaText = Color(0xFFD7CCC8)


@Composable
fun CourseListAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = MilChocolate,
            secondary = MilChocolate,
            background = DarkChocolate,
            onBackground = LightCocoaText // Ensures body text is readable in dark mode
        )

    } else {
        lightColorScheme(
            primary = MilkChocolate,
            secondary = CocoaAccent,
            background = CreamyWhite
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
