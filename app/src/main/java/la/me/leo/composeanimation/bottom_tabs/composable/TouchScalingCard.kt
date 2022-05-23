package la.me.leo.composeanimation.bottom_tabs.composable

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import la.me.leo.composeanimation.ui.theme.surface2dp

@Composable
fun TouchScalingCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface2dp,
    contentColor: Color = contentColorFor(backgroundColor),
    border: BorderStroke? = null,
    restedElevation: Dp = 2.dp,
    touchedElevation: Dp = 6.dp,
    touchedScale: Float,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    var down by remember { mutableStateOf(false) }
    val transition = updateTransition(down, label = "Press state")
    val scale by transition.animateFloat(
        transitionSpec = { tween(150, 0, FastOutSlowInEasing) },
        label = "View scale"
    ) { p -> if (p) touchedScale else 1f }
    val elevation by transition.animateDp(
        transitionSpec = { tween(150, 0, FastOutSlowInEasing) },
        label = "View elevation"
    ) { p -> if (p) touchedElevation else restedElevation }
    Card(
        modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape, backgroundColor, contentColor, border, elevation
    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick)
                .pointerInput(Unit) {
                    while (true) {
                        awaitPointerEventScope {
                            awaitFirstDown(requireUnconsumed = true)
                            down = true
                            waitForUpOrCancellation()
                            down = false
                        }
                    }
                },
            content = content
        )
    }
}
