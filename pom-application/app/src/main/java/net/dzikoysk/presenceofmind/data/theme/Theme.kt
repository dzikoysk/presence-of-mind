package net.dzikoysk.presenceofmind.data.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)

val Teal200 = Color(0xFFADA6F8)

val Gray900 = Color(0xFF121212)
val Gray800 = Color(0xFF202020)
val Gray100 = Color(0xFFf4f5f7)

private val DarkColorPalette = darkColors(
    // primary - light
    primary = Gray100,
    primaryVariant = Color.White,
    onPrimary = Gray900,
    // secondary - purple
    secondary = Purple200,
    secondaryVariant = Teal200,
    onSecondary = Gray900,
    // background
    background = Gray900,
    onBackground = Color.White,
    // surface
    surface = Gray800,
    onSurface = Color.White,
    // error
    error = Color(0xFFB00020),
    onError = Color.White
)

private val LightColorPalette = lightColors(
    // primary - black
    primary = Color.Black,
    primaryVariant = Gray900,
    onPrimary = Color.White,
    // secondary - purple
    secondary = Purple200,
    secondaryVariant = Teal200,
    onSecondary = Gray900,
    // background
    background = Gray100,
    onBackground = Color.Black,
    // surface
    surface = Color.White,
    onSurface = Color.Black,
    // error
    error = Color(0xFFB00020),
    onError = Color.White
)

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontSize = 12.sp
    ),
    subtitle1 = TextStyle(
        fontSize = 12.sp
    )
)

@Composable
fun PresenceOfMindTheme(
    lightTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (lightTheme) LightColorPalette else DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}