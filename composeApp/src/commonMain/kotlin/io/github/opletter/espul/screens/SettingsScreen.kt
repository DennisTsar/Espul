package io.github.opletter.espul.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.opletter.espul.state.EspulViewModel

@Composable
fun SettingsScreen(viewModel: EspulViewModel) {
    Column(Modifier.widthIn(max = 800.dp)) {
        val focusManager = LocalFocusManager.current
        var clearDataDialogOpen by remember { mutableStateOf(false) }
        var githubApiKeyDialogOpen by remember { mutableStateOf(false) }
        ListItem(
            headlineContent = { Text("Followed Users") },
            supportingContent = { Text("Manage followed users") },
            modifier = Modifier.clickable { },
        )
        Divider()
        ListItem(
            headlineContent = { Text("Clear Data") },
            modifier = Modifier.clickable { clearDataDialogOpen = true },
        )
        Divider()
        ListItem(
            headlineContent = { Text("Github API Key") },
            modifier = Modifier.clickable {
                githubApiKeyDialogOpen = true
                // if we don't clear focus, the enter key from the alert textfield will trigger this button again
                // possibly related: https://github.com/JetBrains/compose-multiplatform/issues/1925
                focusManager.clearFocus()
            },
        )

        if (clearDataDialogOpen) {
            AlertDialog(
                onDismissRequest = { clearDataDialogOpen = false },
                title = { Text("Clear Data") },
                text = { Text("Are you sure you want to clear your data?") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearData()
                            clearDataDialogOpen = false
                        }
                    ) {
                        Text("Clear")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { clearDataDialogOpen = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (githubApiKeyDialogOpen) {
            ApiKeyDialog(onDone = { apiKey ->
                if (apiKey != null) {
                    viewModel.setApiKey(apiKey)
                }
                githubApiKeyDialogOpen = false
            })
        }
    }
}

@Composable
fun ApiKeyDialog(onDone: (String?) -> Unit) {
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
            Text("Github API Key", style = MaterialTheme.typography.titleLarge)
            Text(
                buildString {
                    append("No extra permissions needed for increased rate limit. ")
                    append("Must have the relevant Read/Write Repository Contents permission to enable data syncing.")
                }
            )
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("API Key") },
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDone(input) }),
                singleLine = true,
            )
        }
    }
}
