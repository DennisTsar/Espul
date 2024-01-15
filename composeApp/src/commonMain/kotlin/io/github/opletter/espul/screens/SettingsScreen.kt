package io.github.opletter.espul.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import io.github.opletter.espul.state.EspulViewModel

@Composable
fun SettingsScreen(viewModel: EspulViewModel) {
    Column {
        var clearDataDialogOpen by remember { mutableStateOf(false) }
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
            headlineContent = { Text("Followed Users") },
            supportingContent = { Text("Manage followed users") },
            modifier = Modifier.clickable { },
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
    }
}
