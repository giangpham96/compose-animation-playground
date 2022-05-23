package la.me.leo.composeanimation.bottom_tabs

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import la.me.leo.composeanimation.R
import la.me.leo.composeanimation.common.circularReveal
import la.me.leo.composeanimation.bottom_tabs.discovery_list.DiscoveryListScreen
import la.me.leo.composeanimation.bottom_tabs.restaurant_list.RestaurantListScreen
import la.me.leo.composeanimation.ui.theme.Wolt
import la.me.leo.composeanimation.ui.theme.iconPrimary
import la.me.leo.composeanimation.ui.theme.surface8dp

@ExperimentalAnimationApi
@Composable
fun BottomTabScreen() {
    val navController = rememberAnimatedNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController = navController) }) {
        NavigationGraph(navController = navController, contentPadding = it)
    }
}

@ExperimentalAnimationApi
@Composable
private fun NavigationGraph(navController: NavHostController, contentPadding: PaddingValues) {
    AnimatedNavHost(
        navController = navController,
        startDestination = NavigationItem.Discovery.route,
        modifier = Modifier.padding(contentPadding)
    ) {
        tab(navItem = NavigationItem.Discovery, offsetX = 0.25f) { DiscoveryListScreen(modifier = it) }
        tab(navItem = NavigationItem.Restaurant, offsetX = 0.75f) { RestaurantListScreen(modifier = it) }
    }
}

@ExperimentalAnimationApi
private fun NavGraphBuilder.tab(navItem: NavigationItem, offsetX: Float, content: @Composable (Modifier) -> Unit) {
    composable(
        route = navItem.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) { TabCircularRevealContent(offsetX, content) }
}

@ExperimentalAnimationApi
@Composable
private fun AnimatedVisibilityScope.TabCircularRevealContent(offsetX: Float, content: @Composable (Modifier) -> Unit) {
    val transitionSpec = tween<Float>(300, 0, FastOutSlowInEasing)
    val radiusProgress by transition.animateFloat(label = "circular reveal", transitionSpec = { transitionSpec }) {
        when (it) {
            EnterExitState.PreEnter -> 0.33f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> 1f
        }
    }
    val alpha by transition.animateFloat(label = "scale", transitionSpec = { transitionSpec }) {
        when (it) {
            EnterExitState.PreEnter -> 1f
            EnterExitState.Visible -> 1f
            EnterExitState.PostExit -> 0f
        }
    }
    val translationY by transition.animateFloat(label = "translationY", transitionSpec = { transitionSpec }) {
        when (it) {
            EnterExitState.PreEnter -> LocalDensity.current.run { 16.dp.toPx() }
            EnterExitState.Visible -> 0f
            EnterExitState.PostExit -> 0f
        }
    }
    content(
        Modifier
            .graphicsLayer {
                this.alpha = alpha
                this.translationY = translationY
            }
            .circularReveal(radiusProgress, Offset(offsetX, 1f))
    )
}

@Composable
private fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface8dp,
        contentColor = MaterialTheme.colors.iconPrimary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        NavigationItem.values().forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Wolt,
                unselectedContentColor = MaterialTheme.colors.iconPrimary.copy(alpha = 0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}

private enum class NavigationItem(val route: String, val icon: Int, val title: String) {
    Discovery("discovery", R.drawable.ic_tab_discovery, "Discovery"),
    Restaurant("restaurants", R.drawable.ic_tab_delivery, "Restaurants")
}
