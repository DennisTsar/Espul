package io.github.opletter.espul.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.opletter.espul.LocalSnackbarHostState
import io.github.opletter.espul.components.EventCard
import io.github.opletter.espul.components.ImageCard
import io.github.opletter.espul.state.EspulViewModel
import io.github.opletter.espul.state.Event
import io.github.opletter.espul.state.NavState
import kotlinx.datetime.Instant

@Composable
fun FeedScreen(viewModel: EspulViewModel) {
    when (val state = viewModel.navState) {
        is NavState.AllUsers -> AllUsersFeed(viewModel)
        is NavState.UserEvents -> {
            LazyColumn(
                contentPadding = PaddingValues(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(state.events) {
                    EventCard(it, Modifier.widthIn(max = 800.dp))
                }
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AllUsersFeed(viewModel: EspulViewModel) {
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect {
            when (it) {
                is Event.AddUserResult.Success -> {}
                is Event.AddUserResult.UserAlreadyFollowed -> snackbarHostState.showSnackbar("User already followed")
                is Event.AddUserResult.UserNotFound -> snackbarHostState.showSnackbar("User not found")
            }
        }
    }

    val scrollState = rememberScrollState()
    var dialogOpen by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter,
    ) {
        FlowRow(
            Modifier.padding(12.dp).widthIn(max = 800.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val sizeModifier = Modifier.size(150.dp, 175.dp)
            viewModel.followedUsers.forEach { user ->
                Box(sizeModifier) {
                    ImageCard(
                        url = user.avatarUrl,
                        label = user.displayName,
                        onClick = { viewModel.viewUserEvents(user) },
                    )
                    val compareTime = viewModel.userEvents[user.username]?.events?.firstOrNull()?.createdAt
                        ?: Instant.DISTANT_PAST
                    if (user.lastViewed < compareTime) {
                        Badge(
                            Modifier
                                .size(16.dp)
                                .align(Alignment.TopEnd)
                                .offset(4.dp, (-4).dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            val focusManager = LocalFocusManager.current
            if (viewModel.followedUsers.isEmpty()) {
                FilledTonalButton(
                    onClick = {},
                    enabled = false,
                    modifier = sizeModifier,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("You aren't following any users yet. Click the + button to get started.")
                }
            }
            FilledTonalButton(
                onClick = {
                    dialogOpen = true
                    // if we don't clear focus, the enter key from the alert textfield will trigger this button again
                    // possibly related: https://github.com/JetBrains/compose-multiplatform/issues/1925
                    focusManager.clearFocus()
                },
                modifier = sizeModifier.alpha(1f),
                shape = MaterialTheme.shapes.medium,
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    }
    if (dialogOpen) {
        InputDialog(onDone = { username ->
            if (username != null) {
                viewModel.addFollowedUser(username)
            }
            dialogOpen = false
        })
    }
}

@Composable
fun InputDialog(onDone: (String?) -> Unit) {
    Dialog(onDismissRequest = { onDone(null) }) {
        Column(
            Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var input by remember { mutableStateOf("") }
            val focusRequester = remember { FocusRequester() }.apply {
                LaunchedEffect(Unit) { requestFocus() }
            }
            Text("Follow User", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Username") },
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDone(input) }),
                singleLine = true,
            )
        }
    }
}
