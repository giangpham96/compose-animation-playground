package la.me.leo.composeanimation.discovery_list

import la.me.leo.composeanimation.base.BaseViewModel
import la.me.leo.composeanimation.base.BaseViewState
import la.me.leo.composeanimation.composable.VenueLarge
import java.util.UUID

class DiscoveryListViewModel : BaseViewModel<DiscoveryListState>(
    DiscoveryListState(
        list = (1..100).map {
            VenueLarge(
                id = UUID.randomUUID().toString(),
                image = "https://imageproxy.wolt.com/venue/5e7dea0eee5fc1e19b68a674/d75373ec-d0f6-11ec-834a-36e6ac5f1fe1_3a899a10_b1b5_11ec_aa5a_ea0360df7575_wolt_market_venue_discovery_50e.jpg",
                overlayText = null,
                name = "Lalli's Gym - Vallila",
                desc = "Best venue in the world",
                rating5 = 4,
                rating10 = 9.2f,
                deliveryEstimate = "40-50",
                badges = listOf(VenueLarge.Badge(true, "Hot"), VenueLarge.Badge(false, "New")),
                favorite = false,
                deliveryPrice = "$3.90",
                priceRange = 3,
                priceRangeCurrency = '$'
            )
        }
    )
)

data class DiscoveryListState(val list: List<VenueLarge>) : BaseViewState
