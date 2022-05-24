package la.me.leo.composeanimation.bottom_tabs.discovery_list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atMost
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.bottom_tabs.composable.AddressBar
import la.me.leo.composeanimation.ui.theme.Body3
import la.me.leo.composeanimation.ui.theme.Heading4
import la.me.leo.composeanimation.ui.theme.buttonIconicSelected
import la.me.leo.composeanimation.ui.theme.surfaceMain
import la.me.leo.composeanimation.ui.theme.textPrimaryInverse
import kotlin.math.acos
import kotlin.math.sqrt

@Composable
fun DiscoveryHeader(modifier: Modifier, scrollY: Float, onSChanged: (Float) -> Unit) {
    val t = LocalDensity.current.run { 24.dp.toPx() }
    val p = LocalDensity.current.run { 32.dp.toPx() }
    var s by remember { mutableStateOf(0f) }
    var curveHeight by remember { mutableStateOf(0f) }
    val bgProgress = getComponentProgress(scrollY, p + s, p + s + t - LocalDensity.current.run { 8.dp.toPx() })
    val bgAlpha = ((1 - bgProgress) * 255).toInt()
    ConstraintLayout(
        modifier.drawBehind {
            drawRect(Brush.verticalGradient(
                0f to Color(0xF8, 0x68, 0x00, bgAlpha),
                0.7656f to Color(0xF0, 0x9E, 0x00, bgAlpha),
                1f to Color(0xFF, 0xB8, 0x00, bgAlpha)
            ))
        }
    ) {
        val (addressBar, cityState, curve) = createRefs()
        AddressBar(
            tintColor = MaterialTheme.colors.textPrimaryInverse,
            iconBackgroundColor = MaterialTheme.colors.buttonIconicSelected,
            modifier = Modifier
                .constrainAs(addressBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .graphicsLayer {
                    val progress = getComponentProgress(scrollY, p + s, p + s + t - 8.dp.toPx())
                    translationY = -(t - 8.dp.toPx()) * progress * 0.6f
                    alpha = 1 - progress
                }
        )
        CityState(
            modifier = Modifier
                .constrainAs(cityState) {
                    top.linkTo(addressBar.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 72.dp)
                    width = Dimension.fillToConstraints
                }
                .onGloballyPositioned {
                    s = it.size.height.toFloat()
                    onSChanged(it.size.height.toFloat())
                    curveHeight = it.size.height + (4.5f - sqrt(20f)) * it.size.width
                }
                .graphicsLayer {
                    val scale = (0.95f - 1f) * getComponentProgress(scrollY, p / 2, p / 2 + s) + 1f
                    scaleX = scale
                    scaleY = scale
                    alpha = 1 - getComponentProgress(scrollY, s / 2, p + s + t - 8.dp.toPx())
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    translationY = -8.dp.toPx() * getComponentProgress(scrollY, 0f, p + s + t - 8.dp.toPx())
                }
        )
        val h = LocalDensity.current.run { curveHeight.toDp() }
        Curve(
            modifier = Modifier
                .graphicsLayer {
                    translationY = (0 - s) * getComponentProgress(scrollY, 0f, p + s + t - 8.dp.toPx()) + s
                }
                .constrainAs(curve) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(h)
                }
        )
    }
}

@Composable
private fun CityState(modifier: Modifier) {
    ConstraintLayout(modifier) {
        val (title, desc, image) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.donut),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .constrainAs(image) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, 8.dp)
                    height = Dimension.preferredWrapContent.atMost(130.dp)
                }
                .aspectRatio(1f)
        )
        Text(
            text = "Good morning, Helsinki! \uD83D\uDE0B",
            style = Heading4(MaterialTheme.colors.textPrimaryInverse),
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, 8.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(image.start, 8.dp)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = "We will soon be opening for deliveries. In the meantime you can order and pick up yourself or preorder for later.",
            style = Body3(MaterialTheme.colors.textPrimaryInverse),
            modifier = Modifier.constrainAs(desc) {
                top.linkTo(title.bottom, 16.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(image.start, 8.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
private fun Curve(modifier: Modifier) {
    val color = SolidColor(value = MaterialTheme.colors.surfaceMain)
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val r = 4.5f * canvasWidth
        val cx = canvasWidth / 2
        val cy = 4.5f * canvasWidth
        val left = cx - r
        val top = cy - r
        val right = cx + r
        val bottom = cy + r
        val startAngle = 360 - Math.toDegrees(acos(-cx / r).toDouble()).toFloat()
        val endAngle = 360 - Math.toDegrees(acos((canvasWidth - cx) / r).toDouble()).toFloat()
        val path = Path()
        path.addArc(
            oval = Rect(Offset(left, top), Size(right - left, bottom - top)),
            startAngleDegrees = startAngle,
            sweepAngleDegrees = endAngle - startAngle
        )
        path.lineTo(canvasWidth, canvasHeight)
        path.lineTo(0f, canvasHeight)
        path.close()
        drawPath(path, color)
    }
}

private fun getComponentProgress(scroll: Float, start: Float, end: Float): Float {
    check(start <= end) { "start value ($start) must be smaller than end value ($end)" }
    if (start == end) return 0f
    return (scroll - start).coerceIn(0f, end - start) / (end - start)
}
