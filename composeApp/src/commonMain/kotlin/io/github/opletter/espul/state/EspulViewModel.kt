package io.github.opletter.espul.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import io.github.opletter.espul.EspulScreen
import io.github.opletter.espul.gh.data.Author
import io.github.opletter.espul.gh.data.GithubEvent
import io.github.opletter.espul.gh.remote.GithubRepository
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EspulViewModel(private val coroutineScope: CoroutineScope) {
    private val settings = Settings()
    private var repository = GithubRepository(settings.getProp("apiKey"))

    var navState: NavState by mutableStateOf(NavState.AllUsers)
        private set

    var followedUsers by mutableStateOf(settings.getProp<List<FollowedUser>>("followedUsers").orEmpty())
        private set

    var userEvents by mutableStateOf<Map<String, LoadedUserEvents>>(emptyMap())
        private set
    private var prevNavState: NavState? = null

    private val _uiEvents = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = _uiEvents

    val isAuthenticated: Boolean
        get() = repository.isAuthenticated

    fun hasUnreadEvents(user: FollowedUser): Boolean {
        val mostRecentEvent = userEvents[user.username]?.events?.firstOrNull() ?: return false
        return mostRecentEvent.createdAt > user.lastViewed
    }

    fun navItemClicked(screen: EspulScreen) {
        prevNavState = navState
        navState = when (screen) {
            EspulScreen.Feed -> NavState.AllUsers
            EspulScreen.Bookmarks -> NavState.Bookmarks
            EspulScreen.Settings -> NavState.Settings
        }
    }

    fun onBack() {
        prevNavState.takeIf { it !is NavState.UserEvents }?.let {
            navState = it
            prevNavState = null
        }
    }

    fun addFollowedUser(username: String) {
        coroutineScope.launch {
            if (followedUsers.any { it.username == username }) {
                _uiEvents.emit(Event.AddUserResult.UserAlreadyFollowed)
                return@launch
            }
            val user = repository.getUser(username)
            if (user == null) {
                _uiEvents.emit(Event.AddUserResult.UserNotFound)
                return@launch
            }
            val followedUser = FollowedUser(
                username = user.login,
                displayName = user.name ?: user.login,
                avatarUrl = user.avatarUrl,
                lastViewed = Instant.DISTANT_PAST,
            )
            _uiEvents.emit(Event.AddUserResult.Success(followedUser))
            followedUsers = followedUsers + followedUser
            settings.setProp("followedUsers", followedUsers)
            pushSyncData()
            userEvents = userEvents + (followedUser.username to LoadedUserEvents(
                username = followedUser.username,
                events = repository.getUserEvents(followedUser.username),
                upToPage = 1,
            ))
        }
    }

    fun viewUserEvents(user: FollowedUser) {
        prevNavState = navState
        navState = NavState.UserEvents(user, userEvents[user.username]?.events.orEmpty())

        if (!hasUnreadEvents(user)) return

        followedUsers = followedUsers.map {
            if (it.username == user.username) {
                it.copy(lastViewed = Clock.System.now())
            } else {
                it
            }
        }
        settings.setProp("followedUsers", followedUsers)
        pushSyncData()
    }

    fun setApiKey(apiKey: String) {
        repository = GithubRepository(apiKey)
        settings.setProp("apiKey", apiKey)
    }

    fun setSyncRepo(repo: String) {
        val owner = repo.substringBefore("/", "")
        val name = repo.substringAfter("/", "")
        if (owner.isBlank() || name.isBlank()) {
            return
        }
        settings.setProp("syncRepo", repo)
        coroutineScope.launch {
            fetchSyncData()
        }
    }

    fun clearData() {
        followedUsers = emptyList()
        userEvents = emptyMap()
        repository = GithubRepository(null)
        settings.clear()
    }

    init {
        coroutineScope.launch {
            launch { fetchUserEvents() }
            launch { fetchSyncData() }
        }
    }

    private suspend fun fetchUserEvents() {
        userEvents = followedUsers.associate { user ->
            user.username to LoadedUserEvents(
                username = user.username,
                events = repository.getUserEvents(user.username),
                upToPage = 1,
            )
        }
        val state = navState
        if (state is NavState.UserEvents) {
            navState = state.copy(events = userEvents[state.user.username]?.events.orEmpty())
        }
    }

    private suspend fun fetchSyncData() {
        if (!repository.isAuthenticated) return
        val repo = settings.getProp<String>("syncRepo") ?: return
        val owner = repo.substringBefore("/")
        val name = repo.substringAfter("/")
        val response = repository.getRepoFileContent(owner, name, "espul.json") ?: return
        val data = response.content.lines().joinToString("").decodeBase64String()
            .let { Json.decodeFromString<List<FollowedUser>>(it) }
        if (data != followedUsers) {
            followedUsers = data
            fetchUserEvents()
        }
    }

    private fun pushSyncData() {
        if (!repository.isAuthenticated) return
        val repo = settings.getProp<String>("syncRepo") ?: return
        val owner = repo.substringBefore("/")
        val name = repo.substringAfter("/")
        coroutineScope.launch {
            repository.overwriteFileContent(
                owner = owner,
                repo = name,
                path = "espul.json",
                message = "Automatic Update from Espul",
                content = Json.encodeToString(followedUsers),
                committer = Author("espul[bot]@users.noreply.github.com", "Espul[bot]"),
            )
        }
    }
}

sealed interface NavState {
    val screenData: EspulScreen

    sealed interface Feed : NavState {
        override val screenData
            get() = EspulScreen.Feed
    }

    data object AllUsers : Feed
    data class UserEvents(val user: FollowedUser, val events: List<GithubEvent<*>>) : Feed
    data object Bookmarks : NavState {
        override val screenData
            get() = EspulScreen.Bookmarks
    }

    data object Settings : NavState {
        override val screenData
            get() = EspulScreen.Settings
    }
}

sealed interface Event {
    sealed interface AddUserResult : Event {
        data class Success(val user: FollowedUser) : AddUserResult
        data object UserNotFound : AddUserResult
        data object UserAlreadyFollowed : AddUserResult
    }
}

inline fun <reified T> Settings.getProp(key: String): T? {
    return getStringOrNull("espul.$key")?.let { Json.decodeFromString<T?>(it) }
}

inline fun <reified T> Settings.setProp(key: String, value: T) {
    putString("espul.$key", Json.encodeToString(value))
}

@Serializable
data class FollowedUser(
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val lastViewed: Instant,
)

data class LoadedUserEvents(
    val username: String,
    val events: List<GithubEvent<*>>,
    val upToPage: Int,
)
