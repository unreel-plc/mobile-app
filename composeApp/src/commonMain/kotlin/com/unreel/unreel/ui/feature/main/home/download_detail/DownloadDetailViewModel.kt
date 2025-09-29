package com.unreel.unreel.ui.feature.main.home.download_detail

import androidx.lifecycle.viewModelScope
import com.unreel.unreel.core.common.TsmActionViewModel
import com.unreel.unreel.core.common.utils.Resource
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.models.auth.DownloadItem
import com.unreel.unreel.networks.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class DownloadDetailViewModel(
    private val remoteRepository: RemoteRepository,
    private val offlineRepository: OfflineRepository
):  TsmActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = {}
    ) {
        getDownloadItem()
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnBackClicked -> {
                intent {
                    postSideEffect(Event.NavigateBack)
                }
            }
            is Action.OnWatchOnPlatformClicked -> {
                intent {
                    state.downloadItem?.link?.let { link ->
                        postSideEffect(Event.OpenExternalLink(link))
                    }
                }
            }
            is Action.OnRefresh -> {
                getDownloadItem()
            }
            is Action.OnCategoryClicked -> {
                // Handle category click if needed
            }
            is Action.OnTagClicked -> {
                // Handle tag click if needed
            }
        }
    }

    fun setDownloadId(id: String) = intent {
        reduce {
            state.copy(
                id = id
            )
        }
        getDownloadItem()
    }

    private fun getDownloadItem() = intent {
        if (state.id != null) {
            reduce {
                state.copy(isLoading = true, error = null)
            }
            
            viewModelScope.launch(Dispatchers.IO) {
                when (val response = remoteRepository.getDownloadDetail(state.id!!)) {
                    is Resource.Success -> {
                        intent {
                            reduce {
                                state.copy(
                                    downloadItem = response.data,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        intent {
                            reduce {
                                state.copy(
                                    error = response.message ?: "Unknown error",
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class State(
    val id: String? = null,
    val downloadItem: DownloadItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface Action {
    data object OnBackClicked : Action
    data object OnWatchOnPlatformClicked : Action
    data object OnRefresh : Action
    data class OnCategoryClicked(val category: String) : Action
    data class OnTagClicked(val tag: String) : Action
}

sealed interface Event {
    data object NavigateBack : Event
    data class OpenExternalLink(val url: String) : Event
}


