package com.unreel.unreel.ui.feature.auth.login

import com.unreel.unreel.core.common.TsmActionViewModel
import com.unreel.unreel.core.common.utils.LoadingState
import com.unreel.unreel.core.common.utils.Resource
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.repository.RemoteRepository
import kotlinx.coroutines.runBlocking
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class LoginViewModel(
    private val remoteRepository: RemoteRepository,
    private val offlineRepository: OfflineRepository,
) : TsmActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = {},
    ) {
        /* reduce {
             //val phoneNumber = savedStateHandle.get<String>("phoneNumber")?: ""

             state.copy(
                 identifier = "",
             )
         }*/

    }

    fun setPhoneNumber(phoneNumber: String) = intent {
        reduce {
            state.copy(identifier = phoneNumber)
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnIdentifierChanged -> handleOnIdentifierChanged(action)
            is Action.OnDismissError -> handleOnDismissErrorClicked()
            is Action.OnStopLoading -> intent {
                reduce {
                    state.copy(
                        loadingState = LoadingState(false)
                    )
                }
            }

            is Action.OnSendHello -> sendHello()
            is Action.OnGoogleLogin -> googleLogin(action.token)

        }
    }

    private fun sendHello() = intent {
        when (val response =
            remoteRepository.sendHello()) {

            is Resource.Success -> {
                reduce {
                    state.copy(
                        message = response.data!!.message
                    )
                }
            }

            is Resource.Error -> {
                reduce {
                    state.copy(
                        message = response.data.toString()
                    )
                }
            }
        }
    }

    private fun googleLogin(token: String) = intent {
        when (val response = remoteRepository.login(token)) {
            is Resource.Success -> {
                println("Hello, Response  is ${response.data}")
                val token = response.data!!.token ?: ""
                runBlocking {
                    offlineRepository.setAccessToken(token)
                }

                /*reduce {
                    state.copy(
                        user = response.data.user,
                    )
                }*/
                postSideEffect(Event.NavigateToDashboard)
            }

            is Resource.Error -> {
                println("Hello, Error  is ${response.data.toString()}")

                reduce {
                    state.copy(
                        error = response.data.toString()
                    )
                }
            }
        }
    }

    private fun handleOnResetClicked() = intent {
    }

    private fun handleOnDismissNewDeviceDetectedErrorClicked() = intent {

    }

    private fun handleOnIdentifierChanged(action: Action.OnIdentifierChanged) = intent {
        reduce {
            state.copy(
                identifier = action.value,

                )
        }
    }

    private fun handleOnDismissErrorClicked() = intent {
        reduce {
            state.copy(
                error = null,
            )
        }
    }


    /*private fun handleOnLoginClicked() = intent {
        reduce {
            state.copy(
                loadingState = LoadingState(true, "Logging In ... ")
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            when (val response =
                remoteRepository.login(token = state.identifier)) {

                is Resource.Success -> {
                    val refreshToken = response.data!!.refreshToken ?: ""
                    runBlocking {
                        offlineRepository.setAccessToken(response.data!!.accessToken ?: "")
                        offlineRepository.setIdentifier(state.identifier)
                    }
                    postSideEffect(Event.NavigateToDashboard)
                }

                is Resource.Error -> {
                    when (response.statusCode) {

                    }
                }
            }
        }
    }*/
}