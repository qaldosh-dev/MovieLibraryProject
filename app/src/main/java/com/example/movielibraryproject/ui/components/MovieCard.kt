package com.example.movielibraryproject.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.model.Movie

@Composable
fun MovieCard(
    movie: Movie,
    isFavorite: Boolean,
    onMovieClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onMovieClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            if (movie.posterRes != 0) {
                Image(
                    painter = painterResource(id = movie.posterRes),
                    contentDescription = "Movie poster",
                    modifier = Modifier.size(width = 100.dp, height = 140.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "No poster",
                    modifier = Modifier.size(width = 100.dp, height = 140.dp)
                )
            }

            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = movie.year.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = movie.genre,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "★ ${movie.rating}/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                TextButton(
                    onClick = onFavoriteClick
                ) {
                    Text(
                        text = if (isFavorite) "Remove" else "Save"
                    )
                }
            }
        }
    }
}
