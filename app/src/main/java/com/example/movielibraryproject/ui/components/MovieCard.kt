package com.example.movielibraryproject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
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
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Show poster if available; otherwise show a placeholder.
            if (movie.posterRes != 0) {
                Image(
                    painter = painterResource(id = movie.posterRes),
                    contentDescription = stringResource(
                        R.string.movie_card_poster_description
                    ),
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.movie_card_no_poster),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = movie.year.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = movie.genre,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "★ ${movie.rating}/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = onFavoriteClick) {
                    Text(
                        text = stringResource(
                            if (isFavorite) {
                                R.string.movie_card_remove
                            } else {
                                R.string.movie_card_save
                            }
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    MaterialTheme {
        MovieCard(
            movie = Movie(
                id = 1,
                title = "Interstellar",
                year = 2014,
                genre = "Science Fiction",
                rating = 8.7,
                description = "A science fiction movie about space.",
                posterRes = 0
            ),
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
