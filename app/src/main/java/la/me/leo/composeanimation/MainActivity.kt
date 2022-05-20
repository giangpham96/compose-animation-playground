package la.me.leo.composeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import la.me.leo.composeanimation.bottom_tabs.BottomTabScreen
import la.me.leo.composeanimation.discovery_list.DiscoveryListScreen
import la.me.leo.composeanimation.restaurant_list.RestaurantListScreen
import la.me.leo.composeanimation.ui.theme.ComposeTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    BottomTabScreen()
                }
            }
        }
    }
}
