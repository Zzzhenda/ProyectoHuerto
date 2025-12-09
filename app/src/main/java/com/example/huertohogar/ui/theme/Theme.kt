package com.example.huertohogar.ui.theme

import androidx.compose.ui.graphics.Color
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// Esquema de Luz (El principal para tu diseño vibrante)
private val LightColorScheme = lightColorScheme(
    primary = PersimmonOrange,
    onPrimary = OnPersimmon,
    primaryContainer = PersimmonContainer,
    onPrimaryContainer = OnPersimmonContainer,

    secondary = BasilGreen,
    onSecondary = OnBasil,
    secondaryContainer = BasilContainer,
    onSecondaryContainer = OnBasilContainer,

    tertiary = SunflowerYellow,
    onTertiary = OnSunflower,
    tertiaryContainer = SunflowerContainer,
    onTertiaryContainer = OnSunflowerContainer,

    background = OatCream,
    onBackground = OnOat,
    surface = OatSurface,
    onSurface = OnOat,

    error = ErrorRed,
    outline = OutlineColor
)

// Esquema Oscuro (Por si el usuario tiene modo noche, más sobrio)
private val DarkColorScheme = darkColorScheme(
    primary = PersimmonOrange,
    onPrimary = OnPersimmon,
    primaryContainer = Color(0xFF581E00),
    onPrimaryContainer = PersimmonContainer,

    secondary = BasilGreen,
    onSecondary = OnBasilContainer,
    secondaryContainer = Color(0xFF1B361B),
    onSecondaryContainer = BasilContainer,

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5)
)

// Formas más redondeadas y amigables (Original y Simple)
val FriendlyShapes = Shapes(
    small = RoundedCornerShape(12.dp),  // Botones pequeños, inputs
    medium = RoundedCornerShape(20.dp), // Tarjetas (Cards)
    large = RoundedCornerShape(28.dp)   // Contenedores grandes, BottomSheets
)

@Composable
fun HuertoHogarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = false, // Lo desactivamos por defecto para forzar TU diseño original
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Barra de estado del color del fondo para que se sienta inmersivo
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Usamos la tipografía por defecto (Simple)
        shapes = FriendlyShapes,   // Aplicamos nuestras formas redondeadas
        content = content
    )
}