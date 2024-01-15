package io.github.opletter.espul

import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import io.github.opletter.espul.theme.AppTheme


@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .build()
    }
    AppTheme {
        AppContent()
    }
}

@Composable
fun AppContent() {

}
