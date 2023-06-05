package org.like.a.fly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = Color.LightGray,
    onPrimary = Color.Magenta,

    secondary = Color.Gray,
    onSecondary = Color.Cyan,

    tertiary = Color.DarkGray,
    onTertiary = Color.Yellow,

    background = Color.Black,
    onBackground = Color.Magenta,

    surface = Color.Black,
    onSurface = Color.Blue,
)

@Composable
fun LikeAFlyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}