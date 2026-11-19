package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.model.Movie
import com.example.movielibraryproject.ui.components.MovieCard
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme

@Composable
fun FavoritesScreen(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = stringResource(R.string.favorites_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.favorites_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = pluralStringResource(
                    R.plurals.favorites_movie_count,
                    movies.size,
                    movies.size
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (movies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_favorites),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(R.string.favorites_empty_title),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(R.string.favorites_empty_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = movies,
                    key = { movie -> movie.id }
                ) { movie ->
                    MovieCard(
                        movie = movie,
                        isFavorite = true,
                        onMovieClick = {
                            onMovieClick(movie.id)
                        },
                        onFavoriteClick = {
                            onFavoriteClick(movie.id)
                        }
                    )
                }
            }
        }
    }
}

private val previewMovies = listOf(
    Movie(
        id = 1,
        title = "Interstellar",
        year = 2014,
        genre = "Science Fiction",
        rating = 8.7,
        description = "A science fiction movie about space.",
        posterRes = R.drawable.poster_interstellar
    ),
    Movie(
        id = 2,
        title = "The Martian",
        year = 2015,
        genre = "Science Fiction",
        rating = 8.0,
        description = "An astronaut tries to survive on Mars.",
        posterRes = R.drawable.poster_the_martian
    ),
    Movie(
        id = 3,
        title = "Paddington",
        year = 2014,
        genre = "Family",
        rating = 7.3,
        description = "A bear travels to London.",
        posterRes = R.drawable.poster_paddington
    )
)

@Preview(name = "Empty - Light", showBackground = true)
@Composable
private fun FavoritesEmptyLightPreview() {
    MovieLibraryProjectTheme(darkTheme = false) {
        FavoritesScreen(
            movies = emptyList(),
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Empty - Dark", showBackground = true)
@Composable
private fun FavoritesEmptyDarkPreview() {
    MovieLibraryProjectTheme(darkTheme = true) {
        FavoritesScreen(
            movies = emptyList(),
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "One Movie - Light", showBackground = true)
@Composable
private fun FavoritesOneLightPreview() {
    MovieLibraryProjectTheme(darkTheme = false) {
        FavoritesScreen(
            movies = previewMovies.take(1),
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "One Movie - Dark", showBackground = true)
@Composable
private fun FavoritesOneDarkPreview() {
    MovieLibraryProjectTheme(darkTheme = true) {
        FavoritesScreen(
            movies = previewMovies.take(1),
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Several Movies - Light", showBackground = true)
@Composable
private fun FavoritesSeveralLightPreview() {
    MovieLibraryProjectTheme(darkTheme = false) {
        FavoritesScreen(
            movies = previewMovies,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Several Movies - Dark", showBackground = true)
@Composable
private fun FavoritesSeveralDarkPreview() {
    MovieLibraryProjectTheme(darkTheme = true) {
        FavoritesScreen(
            movies = previewMovies,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}
