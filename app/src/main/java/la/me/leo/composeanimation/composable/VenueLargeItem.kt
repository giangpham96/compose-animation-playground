package la.me.leo.composeanimation.composable

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.ui.theme.Body2StrongEmphasis
import la.me.leo.composeanimation.ui.theme.Body3
import la.me.leo.composeanimation.ui.theme.Body3Emphasis
import la.me.leo.composeanimation.ui.theme.ComposeTheme
import la.me.leo.composeanimation.ui.theme.Pepper
import la.me.leo.composeanimation.ui.theme.Salt
import la.me.leo.composeanimation.ui.theme.Tiny
import la.me.leo.composeanimation.ui.theme.Title1
import la.me.leo.composeanimation.ui.theme.Wolt
import la.me.leo.composeanimation.ui.theme.estimateBackground
import la.me.leo.composeanimation.ui.theme.iconPrimary
import la.me.leo.composeanimation.ui.theme.surfaceMain
import la.me.leo.composeanimation.ui.theme.textPrimary
import la.me.leo.composeanimation.ui.theme.textPrimaryInverse
import la.me.leo.composeanimation.ui.theme.textSecondary
import java.util.UUID

@Composable
fun VenueLargeItem(
    venueLarge: VenueLarge,
    onClick: () -> Unit,
    onFavouriteClick: (String, Boolean) -> Unit,
) {
    TouchScalingCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        touchedScale = 0.97f,
        onClick = onClick
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (image, fav, name, desc, estimate, extraInfo, badges) = createRefs()
            VenueLargeImage(
                url = venueLarge.image,
                modifier = Modifier
                    .aspectRatio(2f)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            venueLarge.overlayText?.let {
                val overlay = createRef()
                OverlayText(
                    text = it,
                    modifier = Modifier.constrainAs(overlay) {
                        top.linkTo(image.top)
                        start.linkTo(image.start)
                        end.linkTo(image.end)
                        bottom.linkTo(image.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )
            }
            FavouriteIcon(
                favorite = venueLarge.favorite,
                modifier = Modifier.constrainAs(fav) {
                    top.linkTo(image.top)
                    end.linkTo(image.end)
                },
                onClick = { onFavouriteClick(venueLarge.id, it) }
            )
            BadgeRow(
                badges = venueLarge.badges.take(2),
                modifier = Modifier.constrainAs(badges) {
                    top.linkTo(parent.top, 16.dp)
                    linkTo(start = parent.start, end = fav.start, startMargin = 16.dp, endMargin = 16.dp, bias = 0f)
                }
            )
            Text(
                text = venueLarge.name,
                maxLines = 1,
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(image.bottom, 16.dp)
                    end.linkTo(estimate.start, 16.dp)
                    width = Dimension.fillToConstraints
                },
                style = Title1(MaterialTheme.colors.textPrimary)
            )
            Text(
                text = venueLarge.desc,
                maxLines = 1,
                modifier = Modifier.constrainAs(desc) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(name.bottom)
                    end.linkTo(estimate.start, 16.dp)
                    width = Dimension.fillToConstraints
                },
                style = Body3(MaterialTheme.colors.textPrimary)
            )
            Estimate(venueLarge.deliveryEstimate, Modifier.constrainAs(estimate) {
                top.linkTo(name.top)
                bottom.linkTo(desc.bottom)
                end.linkTo(parent.end, 16.dp)
            })
            val barrier = createBottomBarrier(estimate, desc)
            VenueLargeExtraInfo(venueLarge = venueLarge, modifier = Modifier.constrainAs(extraInfo) {
                top.linkTo(barrier)
                linkTo(start = parent.start, end = parent.end, bias = 0f, startMargin = 16.dp, endMargin = 16.dp)
                bottom.linkTo(parent.bottom)
            })
        }
    }
}

@Composable
private fun VenueLargeImage(url: String, modifier: Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Composable
private fun OverlayText(text: String, modifier: Modifier) {
    Box(modifier = modifier.background(Pepper.copy(alpha = 0.64f))) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = Body2StrongEmphasis(Salt)
        )
    }
}

@Composable
private fun BadgeRow(badges: List<VenueLarge.Badge>, modifier: Modifier) {
    Row(modifier = modifier) {
        badges.mapIndexed { i, badge ->
            if (i > 0) Spacer(modifier = Modifier.size(4.dp))
            BadgeText(badge = badge, modifier = Modifier)
        }
    }
}

@Composable
private fun BadgeText(badge: VenueLarge.Badge, modifier: Modifier) {
    val backgroundColor = if (badge.primary) Wolt else MaterialTheme.colors.surfaceMain
    val textColor =
        if (badge.primary) MaterialTheme.colors.textPrimaryInverse else MaterialTheme.colors.textPrimary
    Text(
        text = badge.text,
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        style = Tiny(textColor)
    )
}

@Composable
private fun FavouriteIcon(favorite: Boolean, modifier: Modifier, onClick: (Boolean) -> Unit) {
    val currentFavourite by rememberUpdatedState(newValue = favorite)
    val transition = updateTransition(currentFavourite, label = "Press state")
    val scale by transition.animateFloat(
        transitionSpec = {
            keyframes {
                durationMillis = 150
                0.8f at 0
                1.2f at 100
                1f at 150
            }
        },
        label = "icon scale"
    ) {
        if (it) 1f else 1f
    }
    val id = if (favorite) R.drawable.ic_m_heart_fill else R.drawable.ic_m_heart
    IconButton(
        onClick = { onClick(!favorite) },
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = null,
            tint = Salt,
            modifier = Modifier
                .padding(4.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(Pepper.copy(alpha = 0.36f), Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .padding(8.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

@Composable
private fun Estimate(deliveryEstimate: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .wrapContentWidth()
            .padding(start = 16.dp)
            .background(
                MaterialTheme.colors.estimateBackground,
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = deliveryEstimate,
            maxLines = 1,
            style = Body3Emphasis(Wolt)
        )
        Text(
            text = "min",
            maxLines = 1,
            style = Tiny(Wolt)
        )
    }
}

@Composable
private fun VenueLargeExtraInfo(venueLarge: VenueLarge, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        if (venueLarge.deliveryPrice != null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_s_delivery_bike),
                contentDescription = null,
                tint = MaterialTheme.colors.iconPrimary,
                modifier = Modifier
            )
            Text(
                text = venueLarge.deliveryPrice,
                style = Body3(MaterialTheme.colors.textSecondary),
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(text = "  ·  ")
        }
        PriceRange(
            priceRange = venueLarge.priceRange,
            priceRangeCurrency = venueLarge.priceRangeCurrency,
            style = Body3(MaterialTheme.colors.textSecondary)
        )
        if (venueLarge.rating5 != null && venueLarge.rating10 != null) {
            Text(text = "  ·  ")
            RatingIcon(rating5 = venueLarge.rating5, rating10 = venueLarge.rating10)
            Text(
                text = String.format("%.1f", venueLarge.rating10),
                style = Body3(MaterialTheme.colors.textSecondary),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(device = Devices.DEFAULT)
@Composable
private fun VenueLargeItemPreview() {
    ComposeTheme(darkTheme = false) {
        VenueLargeItem(
            venueLarge = VenueLarge(
                id = UUID.randomUUID().toString(),
                image = "https://master-wolt-venue-images-cdn.wolt.com/5ca1ead675ac65000c3ca7d6/8a0d0f00dd0e8230d9183530768991eb",
                overlayText = "Closed",
                name = "Lalli's Gym - Vallila",
                desc = "Best venue in the world",
                rating5 = 4,
                rating10 = 9.2f,
                deliveryEstimate = "40-50",
                badges = listOf(VenueLarge.Badge(true, "Hot"), VenueLarge.Badge(false, "New")),
                favorite = true,
                deliveryPrice = "$3.90",
                priceRange = 3,
                priceRangeCurrency = '$'
            ),
            onClick = {},
            onFavouriteClick = { _, _ -> }
        )
    }
}

data class VenueLarge(
    val id: String,
    val image: String,
    val overlayText: String?,
    val name: String,
    val desc: String,
    val rating5: Int?,
    val rating10: Float?,
    val deliveryEstimate: String,
    val badges: List<Badge>,
    val favorite: Boolean,
    val deliveryPrice: String?,
    val priceRange: Int,
    val priceRangeCurrency: Char,
) {

    class Badge(
        val primary: Boolean,
        val text: String,
    )

}
