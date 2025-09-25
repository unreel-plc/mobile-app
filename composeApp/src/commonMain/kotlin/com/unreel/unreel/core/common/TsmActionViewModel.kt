package com.unreel.unreel.core.common

import androidx.lifecycle.ViewModel

abstract class TsmActionViewModel<Action> : ViewModel() {
    abstract fun onAction(action: Action)

}