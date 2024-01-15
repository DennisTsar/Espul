package io.github.opletter.espul.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import io.github.opletter.espul.EspulScreen
import io.github.opletter.espul.gh.data.GithubEvent
import io.github.opletter.espul.gh.remote.GithubRepository
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
    private val repository = GithubRepository(null)

    var navState: NavState by mutableStateOf(NavState.AllUsers)
        private set

    var followedUsers by mutableStateOf(settings.getProp<List<FollowedUser>>("followedUsers").orEmpty())
        private set

    var userEvents by mutableStateOf<Map<String, LoadedUserEvents>>(emptyMap())
        private set
    private var prevNavState: NavState? = null

    private val _uiEvents = MutableSharedFlow<Event>()
    val uiEvents: SharedFlow<Event> = _uiEvents

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
            println("user: $user")
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

        followedUsers = followedUsers.map {
            if (it.username == user.username) {
                it.copy(lastViewed = Clock.System.now())
            } else {
                it
            }
        }
        settings.setProp("followedUsers", followedUsers)
    }

    fun clearData() {
        followedUsers = emptyList()
        userEvents = emptyMap()
        settings.clear()
    }

    init {
        coroutineScope.launch {
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
