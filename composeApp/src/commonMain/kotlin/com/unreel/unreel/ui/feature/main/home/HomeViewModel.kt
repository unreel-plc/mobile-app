package com.unreel.unreel.ui.feature.main.home

import com.unreel.unreel.core.common.TsmActionViewModel
import com.unreel.unreel.core.datastore.OfflineRepository
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
    ) {}

    override fun onAction(action: Action) {

    }
}

data class State(
    val username: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String = "Shorts",
    val contentItems: List<ContentItem> = emptyList(),
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean = true
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
}

sealed interface Event {

}
