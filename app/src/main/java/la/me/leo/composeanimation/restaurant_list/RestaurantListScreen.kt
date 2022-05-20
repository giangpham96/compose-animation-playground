package la.me.leo.composeanimation.restaurant_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.composable.AddressBar
import la.me.leo.composeanimation.composable.CollapsedHeaderBackground
import la.me.leo.composeanimation.composable.CollapsedHeaderContent
import la.me.leo.composeanimation.composable.VenueLarge
import la.me.leo.composeanimation.composable.VenueLargeItem
import la.me.leo.composeanimation.ui.theme.Heading4
import la.me.leo.composeanimation.ui.theme.Wolt
import la.me.leo.composeanimation.ui.theme.buttonIconic
import la.me.leo.composeanimation.ui.theme.buttonIconicSelected
import la.me.leo.composeanimation.ui.theme.iconPrimary
import la.me.leo.composeanimation.ui.theme.surfaceMain
import la.me.leo.composeanimation.ui.theme.textPrimary

@Composable
fun RestaurantListScreen(viewModel: RestaurantListViewModel = viewModel(), modifier: Modifier) {
    val state = viewModel.state

    val listState = rememberLazyListState()
    var collapsingDistance by remember { mutableStateOf(0f) }
    var headerHeight by remember { mutableStateOf(0) }
    val collapseHeight = LocalDensity.current.run { 56.dp.toPx() }

    val scrollProgress: Float by remember {
        derivedStateOf {
            when {
                collapsingDistance == 0f -> 0f
                listState.firstVisibleItemIndex > 0 -> 1f
                else -> (listState.firstVisibleItemScrollOffset / collapsingDistance).coerceIn(0f, 1f)
            }
        }
    }

    LaunchedEffect(state.loading) {
        if (state.loading) listState.animateScrollToItem(0, 0)
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surfaceMain)
    ) {

        val (expandedHeader, mapIcon, progress) = createRefs()

        ExpandedHeader(
            Modifier
                .constrainAs(expandedHeader) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .onGloballyPositioned { layoutCoordinates ->
                    headerHeight = layoutCoordinates.size.height
                    collapsingDistance = layoutCoordinates.size.height - collapseHeight
                }
                .graphicsLayer {
                    translationY = -collapsingDistance * scrollProgress
                    alpha = 1 - scrollProgress
                }
        )

        AnimatedVisibility(
            visible = state.loading,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.constrainAs(progress) {
                top.linkTo(expandedHeader.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
        ) {
            CircularProgressIndicator()
        }

        AnimatedVisibility(visible = !state.loading, enter = fadeIn(), exit = fadeOut()) {
            RestaurantList(
                state = listState,
                headerHeight = headerHeight,
                list = state.list
            ) { id, fav ->
                viewModel.onFavoriteChange(id, fav)
            }
            CollapsedHeaderBackground(
                progress = scrollProgress,
                modifier = Modifier
            )
            CollapsedHeaderContent(
                progress = scrollProgress,
                modifier = Modifier
            )
        }
        MapIcon(
            modifier = Modifier
                .displayCutoutPadding()
                .statusBarsPadding()
                .constrainAs(mapIcon) {
                    top.linkTo(parent.top, 8.dp)
                    end.linkTo(parent.end, 16.dp)
                },
            onClick = { viewModel.reload() }
        )
    }
}

@Composable
private fun ExpandedHeader(modifier: Modifier) {
    Column(modifier) {
        AddressBar(tintColor = Wolt, iconBackgroundColor = MaterialTheme.colors.buttonIconicSelected)
        Text(
            text = "Restaurants",
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            style = Heading4(MaterialTheme.colors.textPrimary)
        )
    }
}

@Composable
private fun MapIcon(modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .width(40.dp)
            .height(40.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_map),
            contentDescription = null,
            modifier = modifier
                .background(
                    color = MaterialTheme.colors.buttonIconic,
                    shape = CircleShape
                )
                .padding(10.dp),
            tint = MaterialTheme.colors.iconPrimary
        )
    }
}

@Composable
private fun RestaurantList(
    state: LazyListState,
    headerHeight: Int,
    list: List<VenueLarge>,
    onFavouriteClick: (String, Boolean) -> Unit,
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = LocalDensity.current.run { headerHeight.toDp() }
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier,
    ) {
        items(list, key = { it.id }) { venueLarge ->
            VenueLargeItem(
                venueLarge = venueLarge,
                onClick = {},
                onFavouriteClick = onFavouriteClick
            )
        }
    }
}

