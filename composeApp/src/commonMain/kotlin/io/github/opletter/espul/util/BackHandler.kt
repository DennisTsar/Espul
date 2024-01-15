package io.github.opletter.espul.util

import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

@Composable
fun DoubleBackHandler(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var isBackPressed by remember { mutableStateOf(false) }
    BackHandler(!isBackPressed) {
        onBack()
        isBackPressed = true
        scope.launch {
            delay(2000L)
            isBackPressed = false
        }
    }
}
