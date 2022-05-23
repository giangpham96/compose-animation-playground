package la.me.leo.composeanimation.bottom_tabs.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.ui.theme.Body3Emphasis
import la.me.leo.composeanimation.ui.theme.Title1
import la.me.leo.composeanimation.ui.theme.Wolt
import la.me.leo.composeanimation.ui.theme.surface4dp
import la.me.leo.composeanimation.ui.theme.textPrimary

@Composable
fun CollapsedHeaderContent(progress: Float, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(end = 56.dp, start = 16.dp)
            .fillMaxWidth()
            .displayCutoutPadding()
            .statusBarsPadding()
            .height(56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val titleTranslationY0 = LocalDensity.current.run { -12.dp.toPx() }
        val subtitleTranslationY0 = LocalDensity.current.run { -10.dp.toPx() }
        Text(
            text = "Discovery",
            style = Title1(MaterialTheme.colors.textPrimary),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f, fill = false)
                .graphicsLayer {
                    val titleAlphaProgress = getComponentProgress(progress, 0.35f, 0.9f)
                    alpha = titleAlphaProgress
                    val titleTranslationYProgress = getComponentProgress(progress, 0.35f, 1f)
                    translationY = titleTranslationY0 * (1 - titleTranslationYProgress)
                }
        )
        Row(
            modifier = Modifier.graphicsLayer {
                val subTitleAlphaProgress = getComponentProgress(progress, 0.25f, 0.8f)
                alpha = subTitleAlphaProgress
                val subTitleTranslationYProgress = getComponentProgress(progress, 0.25f, 1f)
                translationY = subtitleTranslationY0 * (1 - subTitleTranslationYProgress)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Arkadiankatu 6",
                style = Body3Emphasis(Wolt),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = Wolt
            )
        }
    }
}

@Composable
fun CollapsedHeaderBackground(progress: Float, modifier: Modifier) {
    val translationY0 = LocalDensity.current.run { -8.dp.toPx() }
    Surface(
        elevation = 4.dp,
        color = MaterialTheme.colors.surface4dp,
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = getComponentProgress(progress, 0.15f, 0.25f)
                val translationYProgress = getComponentProgress(progress, 0.15f, 1f)
                translationY = translationY0 * (1 - translationYProgress)
            }
    ) {
        Box(
            modifier = Modifier
            .displayCutoutPadding()
            .statusBarsPadding()
            .height(56.dp)
        )
    }
}

private fun getComponentProgress(progress: Float, start: Float, end: Float): Float {
    check(start < end) { "start value ($start) must be smaller than end value ($end)" }
    return (progress - start).coerceIn(0f, end - start) / (end - start)
}
