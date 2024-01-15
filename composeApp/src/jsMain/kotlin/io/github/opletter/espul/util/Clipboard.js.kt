package io.github.opletter.espul.util

import kotlinx.browser.window
import kotlinx.coroutines.await

actual suspend fun getClipboardText(): String? = window.navigator.clipboard.readText().await()
