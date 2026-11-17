package com.example.movielibraryproject.ui.screens

import androidx.compose.runtime.Composable
import com.example.movielibraryproject.model.Movie
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.movielibraryproject.R
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.movielibraryproject.ui.components.MovieCard
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun FavoritesScreen(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit
) {
    if (movies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.favorites_empty_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.favorites_empty_message),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
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
@Preview(showBackground = true)
@Composable
fun FavoritesScreenEmptyPreview() {
    MaterialTheme {
        FavoritesScreen(
            movies = emptyList(),
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenFilledPreview() {
    val sampleMovies = listOf(
        Movie(
            id = 1,
            title = "Interstellar",
            year = 2014,
            genre = "Science Fiction",
            rating = 8.7,
            description = "A science fiction movie about space.",
            posterRes = 0
        ),
        Movie(
            id = 2,
            title = "The Martian",
            year = 2015,
            genre = "Science Fiction",
            rating = 8.0,
            description = "An astronaut tries to survive on Mars.",
            posterRes = 0
        )
    )

    MaterialTheme {
        FavoritesScreen(
            movies = sampleMovies,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}