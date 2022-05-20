package la.me.leo.composeanimation.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<VS : BaseViewState>(defaultState: VS) : ViewModel() {

    var state by mutableStateOf(defaultState)
        protected set

}

interface BaseViewState
