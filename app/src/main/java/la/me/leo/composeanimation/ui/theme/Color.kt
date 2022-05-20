package la.me.leo.composeanimation.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val Pepper = Color(0xFF202125)
val Salt = Color(0xFFFFFFFF)
val Wolt = Color(0xFF009DE0)
val Space = Color(0xFF000000)
val Orange = Color(0xFFFF9800)

val Colors.textPrimary: Color
    @Composable get() = if (isLight) Pepper else Salt

val Colors.textSecondary: Color
    @Composable get() = if (isLight) Pepper.copy(alpha = 0.64f) else Salt.copy(alpha = 0.72f)

val Colors.priceRangeUnhighlighted: Color
    @Composable get() = if (isLight) Pepper.copy(alpha = 0.24f) else Salt.copy(alpha = 0.28f)

val Colors.buttonIconic: Color
    @Composable get() = if (isLight) Pepper.copy(alpha = 0.08f) else Salt.copy(alpha = 0.12f)

val Colors.buttonIconicSelected: Color
    @Composable get() = if (isLight) Wolt.copy(alpha = 0.08f) else Wolt.copy(alpha = 0.12f)

val Colors.textPrimaryInverse: Color
    @Composable get() = if (isLight) Salt else Pepper

val Colors.surfaceMain: Color
    @Composable get() = if (isLight) Salt else Space

val Colors.surface2dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.09f).compositeOver(Space)

val Colors.surface4dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.11f).compositeOver(Space)

val Colors.surface8dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.13f).compositeOver(Space)

val Colors.surface12dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.14f).compositeOver(Space)

val Colors.surface16dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.15f).compositeOver(Space)

val Colors.surface24dp: Color
    @Composable get() = if (isLight) Salt else Salt.copy(alpha = 0.16f).compositeOver(Space)

val Colors.divider: Color
    @Composable get() = if (isLight) Pepper.copy(alpha = 0.08f) else Salt.copy(alpha = 0.12f)

val Colors.iconPrimary: Color
    @Composable get() = if (isLight) Pepper else Salt

val Colors.estimateBackground: Color
    @Composable get() = if (isLight) Pepper.copy(alpha = 0.04f) else Salt.copy(alpha = 0.12f)
