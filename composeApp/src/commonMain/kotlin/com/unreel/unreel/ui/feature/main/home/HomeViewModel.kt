package com.unreel.unreel.ui.feature.main.home

import com.unreel.unreel.core.common.TsmActionViewModel
import com.unreel.unreel.core.common.utils.Resource
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.models.auth.DownloadItem
import com.unreel.unreel.networks.repository.RemoteRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container


class HomeViewModel(
    private val remoteRepository: RemoteRepository,
    private val offlineRepository: OfflineRepository
) : TsmActionViewModel<Action>(), ContainerHost<State, Event> {
    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = {}
    ) {
        getDownloadedItems()
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnCategorySelected -> {
                getDownloadedItems()
            }
            is Action.OnContentClicked -> intent {
                postSideEffect(Event.GoToDownloadDetail(action.contentId))
            }
            is Action.OnRefresh -> intent {
                getDownloadedItems()
            }
            else -> {

            }
        }
    }

    fun getDownloadedItems() = intent {
        reduce { state.copy(isLoading = true)}
        when (val response = remoteRepository.getDownloads()) {
            is Resource.Success -> {
                println("Home Screen, ${response.data}")
                reduce {
                    state.copy(
                        downloadedItems = response.data?.results ?: emptyList(),
                        isLoading = false
                    )
                }
            }
            is Resource.Error -> {
                println("Home Screen, ${response.message}")

                state.copy(
                    error = response.message,
                    isLoading = false
                )
            }
        }
    }
}

data class State(
    val username: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "",
    val contentItems: List<ContentItem> = emptyList(),
    val downloadedItems: List<DownloadItem> = emptyList(),
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean = true,

    val error: String? = null,
)

data class ContentItem(
    val id: String,
    val title: String,
    val creator: String,
    val thumbnailUrl: String,
    val viewCount: String,
    val duration: String? = null,
    val isVerified: Boolean = false
)

sealed interface Action {
    data class OnSearchQueryChanged(val query: String) : Action
    data class OnCategorySelected(val category: String) : Action
    data class OnContentClicked(val contentId: String) : Action
    data object OnSearchClicked : Action
    data object OnThemeToggleClicked : Action

    object OnRefresh: Action
}

sealed interface Event {
    data class GoToDownloadDetail(val id: String): Event
}
