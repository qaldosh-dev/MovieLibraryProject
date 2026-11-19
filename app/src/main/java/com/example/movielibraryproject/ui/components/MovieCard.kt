package com.example.movielibraryproject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.model.Movie
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

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
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (movie.posterRes != 0) {
                Image(
                    painter = painterResource(movie.posterRes),
                    contentDescription = stringResource(
                        R.string.movie_card_poster_description
                    ),
                    modifier = Modifier
                        .size(width = 84.dp, height = 126.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 84.dp, height = 126.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.movie_card_no_poster),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.genre,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = movie.year.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "★ ${movie.rating}/10",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    contentDescription = stringResource(
                        R.string.movie_card_poster_description,
                        movie.title
                    ),
                    tint = if (isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
private val previewMovie = Movie(
    id = 1,
    title = "Interstellar",
    year = 2014,
    genre = "Science Fiction",
    rating = 8.7,
    description = "A science fiction movie about space.",
    posterRes = R.drawable.poster_interstellar
)

@Preview(showBackground = true, name = "Movie Card")
@Composable
private fun MovieCardPreview() {
    MovieLibraryProjectTheme {
        MovieCard(previewMovie, false, {}, {})
    }
}

@Preview(showBackground = true, name = "Favorite Movie Card")
@Composable
private fun FavoriteMovieCardPreview() {
    MovieLibraryProjectTheme {
        MovieCard(previewMovie, true, {}, {})
    }
}

@Preview(showBackground = true, name = "Long Movie Title")
@Composable
private fun LongMovieTitlePreview() {
    MovieLibraryProjectTheme {
        MovieCard(
            movie = previewMovie.copy(
                title = "The Extraordinary Adventures of a Very Long Movie Title"
            ),
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}
@Preview(
    showBackground = true,
    name = "Narrow Movie Card",
    widthDp = 320
)
@Composable
private fun NarrowMovieCardPreview() {
    MovieLibraryProjectTheme {
        MovieCard(
            movie = previewMovie,
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Preview(showBackground = true, name = "Movie Card - Light Theme")
@Composable
private fun MovieCardLightPreview() {
    MovieLibraryProjectTheme(darkTheme = false) {
        MovieCard(
            movie = previewMovie,
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Movie Card - Dark Theme")
@Composable
private fun MovieCardDarkPreview() {
    MovieLibraryProjectTheme(darkTheme = true) {
        MovieCard(
            movie = previewMovie,
            isFavorite = true,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}
@Preview(
    showBackground = true,
    name = "Large Font Movie Card",
    widthDp = 360
)
@Composable
private fun LargeFontMovieCardPreview() {
    MovieLibraryProjectTheme {
        CompositionLocalProvider(
            LocalDensity provides Density(
                density = LocalDensity.current.density,
                fontScale = 1.5f
            )
        ) {
            MovieCard(
                movie = previewMovie,
                isFavorite = false,
                onMovieClick = {},
                onFavoriteClick = {}
            )
        }
    }
}