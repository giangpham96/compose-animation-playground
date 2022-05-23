package la.me.leo.composeanimation.bottom_tabs.composable

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import la.me.leo.composeanimation.ui.theme.priceRangeUnhighlighted

@Composable
fun PriceRange(
    priceRange: Int,
    priceRangeCurrency: Char,
    prefix: String = "",
    suffix: String = "",
    style: TextStyle
) {
    val displayedText = buildAnnotatedString {
        append(prefix)
        repeat(priceRange) {
            append(priceRangeCurrency)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colors.priceRangeUnhighlighted)) {
            repeat(4 - priceRange) {
                append(priceRangeCurrency)
            }
        }
        append(suffix)
    }
    Text(
        text = displayedText,
        style = style
    )
}
