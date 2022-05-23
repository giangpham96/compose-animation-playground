package la.me.leo.composeanimation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import la.me.leo.composeanimation.bottom_tabs.BottomTabScreen

@ExperimentalAnimationApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Box(
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationGraph(navController = navController)
    }
}

@ExperimentalAnimationApi
@Composable
private fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "showCase",
    ) {
        composable("showCase") {
            ShowCasesScreen(navController)
        }
        composable("tabs") {
            BottomTabScreen()
        }
    }
}

