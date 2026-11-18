package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.ui.components.MovieCard
import com.example.movielibraryproject.viewmodel.MovieUiState
import androidx.compose.ui.tooling.preview.Preview
import com.example.movielibraryproject.data.MovieRepository
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme

@Composable
fun CatalogScreen(
    uiState: MovieUiState,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onClearGenre: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.catalog_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.catalog_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onQueryChange,
            label = {
                Text(
                    text = stringResource(R.string.catalog_search_label)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.catalog_search_placeholder)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            singleLine = true
        )

        val selectedGenre = uiState.selectedGenre

        if (selectedGenre != null) {
            Text(
                text = stringResource(
                    R.string.catalog_selected_genre,
                    selectedGenre
                ),
                style = MaterialTheme.typography.titleMedium
            )

            TextButton(onClick = onClearGenre) {
                Text(
                    text = stringResource(R.string.catalog_clear_genre)
                )
            }
        }

        val movies = uiState.filteredMovies

        if (movies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.catalog_no_results),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = movies,
                    key = { movie -> movie.id }
                ) { movie ->
                    MovieCard(
                        movie = movie,
                        isFavorite = movie.id in uiState.favoriteIds,
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

@Composable
private fun CatalogPreviewContent(uiState: MovieUiState) {
    MovieLibraryProjectTheme {
        CatalogScreen(
            uiState = uiState,
            onQueryChange = {},
            onMovieClick = {},
            onFavoriteClick = {},
            onClearGenre = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {
    CatalogPreviewContent(
        uiState = MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres,
            favoriteIds = setOf(1)
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogGenrePreview() {
    CatalogPreviewContent(
        uiState = MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres,
            selectedGenre = "Comedy"
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CatalogNoResultsPreview() {
    CatalogPreviewContent(
        uiState = MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres,
            searchQuery = "unknown movie"
        )
    )
}