package com.example.movielibraryproject.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.movielibraryproject.R
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.AsyncImagePainter

@Composable
fun NetworkPoster(
    posterUrl: String?,
    contentDescription: String?,
    posterRes: Int = 0,
    modifier: Modifier = Modifier
) {
    when {
        !posterUrl.isNullOrBlank() -> {
            SubcomposeAsyncImage(
                model = posterUrl,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> PosterPlaceholder(
                        text = stringResource(R.string.poster_loading),
                        modifier = Modifier.fillMaxSize()
                    )

                    is AsyncImagePainter.State.Error,
                    is AsyncImagePainter.State.Empty -> PosterPlaceholder(
                        text = stringResource(R.string.movie_card_no_poster),
                        modifier = Modifier.fillMaxSize()
                    )

                    else -> SubcomposeAsyncImageContent()
                }
            }
        }

        posterRes != 0 -> {
            Image(
                painter = painterResource(posterRes),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }

        else -> PosterPlaceholder(
            text = stringResource(R.string.movie_card_no_poster),
            modifier = modifier
        )
    }
}

@Composable
private fun PosterPlaceholder(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
