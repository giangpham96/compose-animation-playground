package la.me.leo.composeanimation.bottom_tabs.discovery_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import la.me.leo.composeanimation.bottom_tabs.composable.CollapsedHeaderBackground
import la.me.leo.composeanimation.bottom_tabs.composable.CollapsedHeaderContent
import la.me.leo.composeanimation.bottom_tabs.composable.VenueLarge
import la.me.leo.composeanimation.bottom_tabs.composable.VenueLargeItem
import la.me.leo.composeanimation.ui.theme.surfaceMain

@Composable
fun DiscoveryListScreen(viewModel: DiscoveryListViewModel = viewModel(), modifier: Modifier) {
    val state = viewModel.state

    val t = LocalDensity.current.run { 24.dp.toPx() }
    val p = LocalDensity.current.run { 32.dp.toPx() }
    val e = LocalDensity.current.run { 8.dp.toPx() }
    var s by remember { mutableStateOf(0f) }
    var headerHeight by remember { mutableStateOf(0) }

    val listState = rememberLazyListState()

    val scrollY: Float by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                p + s + t + e
            } else {
                listState.firstVisibleItemScrollOffset.toFloat()
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colors.surfaceMain)
    ) {
        val (backHeader, frontHeaderBg, frontHeaderContent, list) = createRefs()
        DiscoveryHeader(
            modifier = Modifier
                .constrainAs(backHeader) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .onGloballyPositioned {
                    headerHeight = it.size.height
                },
            scrollY = scrollY,
            onSChanged = { s = it }
        )
        DiscoveryList(
            state = listState,
            headerHeight = headerHeight,
            list = state.list,
            modifier = Modifier.constrainAs(list) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )
        CollapsedHeaderBackground(
            progress = getComponentProgress(scrollY, t + p + s - e, t + p + s + e),
            modifier = Modifier
                .constrainAs(frontHeaderBg) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
        CollapsedHeaderContent(
            progress = getComponentProgress(scrollY, t + p + s - e, t + p + s + e),
            modifier = Modifier
                .constrainAs(frontHeaderContent) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )
    }
}

@Composable
private fun DiscoveryList(
    state: LazyListState,
    headerHeight: Int,
    list: List<VenueLarge>,
    modifier: Modifier,
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp,
            top = LocalDensity.current.run { (headerHeight.toDp().value - 40).coerceAtLeast(0f).dp }
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier,
    ) {
        items(list, key = { it.id }) { venueLarge ->
            VenueLargeItem(
                venueLarge = venueLarge,
                onClick = {},
                onFavouriteClick = { _, _ -> }
            )
        }
    }
}

private fun getComponentProgress(scroll: Float, start: Float, end: Float): Float {
    check(start < end) { "start value ($start) must be smaller than end value ($end)" }
    return (scroll - start).coerceIn(0f, end - start) / (end - start)
}
