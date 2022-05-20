package la.me.leo.composeanimation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Body2StrongEmphasis = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.01.sp,
    lineHeight = 20.sp
)

private val Tiny = TextStyle(
    fontSize = 10.sp,
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    letterSpacing = 0.0124.sp,
    lineHeight = 12.sp
)

private val Title1 = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 0.01.sp,
    lineHeight = 20.sp
)

private val Body3 = TextStyle(
    fontSize = 14.sp,
    letterSpacing = 0.009.sp,
    lineHeight = 20.sp
)

private val Body3Emphasis = Body3.copy(
    fontWeight = FontWeight.Medium,
    fontFamily = FontFamily.SansSerif,
)

private val Heading4 = TextStyle(
    fontSize = 28.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 32.sp
)

fun Body2StrongEmphasis(color: Color) = Body2StrongEmphasis.copy(color = color)

fun Tiny(color: Color) = Tiny.copy(color = color)

fun Title1(color: Color) = Title1.copy(color = color)

fun Body3(color: Color) = Body3.copy(color = color)

fun Body3Emphasis(color: Color) = Body3Emphasis.copy(color = color)

fun Heading4(color: Color) = Heading4.copy(color = color)
