package io.github.opletter.espul.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest

@Composable
fun ImageCard(url: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            var imageLoaded by remember { mutableStateOf(false) }
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(url)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                onSuccess = { imageLoaded = true },
            )
            if (imageLoaded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.85f),
                                )
                            )
                        )
                )
            }
            Text(
                text = "Hello World",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp),
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
            )
        }
    }
}
