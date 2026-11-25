package com.example.movielibraryproject.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.AsyncImagePainter

@Composable
fun NetworkPoster(
    posterUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = posterUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }
            }

            is AsyncImagePainter.State.Error,
            is AsyncImagePainter.State.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No poster")
                }
            }

            else -> SubcomposeAsyncImageContent()
        }
    }
}
