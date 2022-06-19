package la.me.leo.composeanimation.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import kotlin.math.sqrt

/**
 * Copied from https://gist.github.com/darvld/eb3844474baf2f3fc6d3ab44a4b4b5f8
 * Credits go to Dario Valdespino
 */
fun Modifier.circularReveal(
    transitionProgress: Float,
    revealFrom: Offset = Offset(0.5f, 0.5f)
): Modifier {
    return drawWithCache {
        val path = Path()

        val center = revealFrom.mapTo(size)
        val radius = calculateRadius(revealFrom, size)

        path.addOval(Rect(center, radius * transitionProgress))

        onDrawWithContent {
            clipPath(path) { this@onDrawWithContent.drawContent() }
        }
    }
}

private fun Offset.mapTo(size: Size): Offset {
    return Offset(x * size.width, y * size.height)
}

private fun calculateRadius(normalizedOrigin: Offset, size: Size) = with(normalizedOrigin) {
    val x = (if (x > 0.5f) x else 1 - x) * size.width
    val y = (if (y > 0.5f) y else 1 - y) * size.height

    sqrt(x * x + y * y)
}

