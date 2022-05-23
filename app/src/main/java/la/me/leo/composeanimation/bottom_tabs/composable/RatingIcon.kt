package la.me.leo.composeanimation.bottom_tabs.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.ui.theme.iconPrimary

@Composable
fun RatingIcon(rating5: Int, rating10: Float) {
    require(rating5 in 0..4 && rating10 in 0f..10f)
    val colored = rating10 >= 9 && rating5 == 4
    val iconId = when (rating5) {
        0 -> R.drawable.ic_smiley_disgusted_outline
        1 -> R.drawable.ic_smiley_sad_outline
        2 -> R.drawable.ic_smiley_meh_outline
        3 -> R.drawable.ic_smiley_happy_outline
        4 -> {
            if (colored) {
                R.drawable.ic_smiley_excited
            } else {
                R.drawable.ic_smiley_excited_outline
            }
        }
        else -> throw IllegalStateException()
    }
    if (colored) {
        Image(painter = painterResource(id = iconId), contentDescription = null)
    } else {
        val alpha = if (isSystemInDarkTheme()) 0.72f else 0.64f
        val tint = MaterialTheme.colors.iconPrimary.copy(alpha = alpha)
        Icon(painter = painterResource(id = iconId), contentDescription = null, tint = tint)
    }
}
