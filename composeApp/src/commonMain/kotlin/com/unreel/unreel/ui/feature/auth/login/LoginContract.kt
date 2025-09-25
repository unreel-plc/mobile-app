package com.unreel.unreel.ui.feature.auth.login

import com.unreel.unreel.core.common.utils.LoadingState
import com.unreel.unreel.networks.models.auth.UserModel

data class State(
    val identifier: String = "",
    val loadingState: LoadingState = LoadingState(),
    val error: String? = null,

    val user: UserModel? = null,

    val message: String? = null,
)
sealed interface Action {
    data class OnIdentifierChanged(val value: String) : Action
    object OnDismissError : Action
    object OnStopLoading : Action
    object OnSendHello: Action
    data class OnGoogleLogin(val token: String): Action
}

sealed interface Event {
    object NavigateToDashboard : Event
}