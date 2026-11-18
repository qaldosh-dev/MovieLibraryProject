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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.movielibraryproject.model.Movie
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CatalogScreen(
    uiState: MovieUiState,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onClearGenre: () -> Unit
) {
    val movies = uiState.filteredMovies
    val selectedGenre = uiState.selectedGenre
    val featuredMovie = uiState.movies.firstOrNull()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "catalog_heading") {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
            }
        }

        item(key = "catalog_search") {
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
                        text = stringResource(
                            R.string.catalog_search_placeholder
                        )
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        TextButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.catalog_clear_search
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                singleLine = true
            )
        }

        if (selectedGenre != null) {
            item(key = "catalog_genre") {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(
                                R.string.catalog_selected_genre,
                                selectedGenre
                            ),
                            style = MaterialTheme.typography.titleMedium
                        )

                        TextButton(onClick = onClearGenre) {
                            Text(
                                text = stringResource(
                                    R.string.catalog_clear_genre
                                )
                            )
                        }
                    }
                }
            }
        }

        if (
            uiState.searchQuery.isBlank() &&
            selectedGenre == null &&
            featuredMovie != null
        ) {
            item(key = "featured_movie") {
                FeaturedMovieCard(
                    movie = featuredMovie,
                    onClick = { onMovieClick(featuredMovie.id) }
                )
            }
        }

        item(key = "results_heading") {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(
                        if (
                            uiState.searchQuery.isBlank() &&
                            selectedGenre == null
                        ) {
                            R.string.catalog_all_movies
                        } else {
                            R.string.catalog_search_results
                        }
                    ),
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = pluralStringResource(
                        R.plurals.catalog_results_count,
                        movies.size,
                        movies.size
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (movies.isEmpty()) {
            item(key = "catalog_empty") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.catalog_no_results),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(
                            if (
                                uiState.searchQuery.isNotEmpty() ||
                                selectedGenre != null
                            ) {
                                R.string.catalog_no_results_hint
                            } else {
                                R.string.catalog_empty_library_hint
                            }
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    if (uiState.searchQuery.isNotEmpty()) {
                        TextButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.catalog_clear_search
                                )
                            )
                        }
                    }
                }
            }
        } else {
            items(
                items = movies,
                key = { movie -> movie.id }
            ) { movie ->
                MovieCard(
                    movie = movie,
                    isFavorite = movie.id in uiState.favoriteIds,
                    onMovieClick = { onMovieClick(movie.id) },
                    onFavoriteClick = { onFavoriteClick(movie.id) }
                )
            }
        }
    }
}

@Composable
private fun FeaturedMovieCard(
    movie: Movie,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(movie.posterRes),
                contentDescription = stringResource(
                    R.string.catalog_poster_description,
                    movie.title
                ),
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.catalog_featured),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = stringResource(
                        R.string.catalog_movie_info,
                        movie.year,
                        movie.genre
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = stringResource(
                        R.string.catalog_rating,
                        movie.rating
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun CatalogPreviewContent(
    uiState: MovieUiState,
    darkTheme: Boolean = false
) {
    MovieLibraryProjectTheme(
        darkTheme = darkTheme,
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CatalogScreen(
                uiState = uiState,
                onQueryChange = {},
                onMovieClick = {},
                onFavoriteClick = {},
                onClearGenre = {}
            )
        }
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

@Preview(
    name = "Catalog - Dark",
    showBackground = true
)
@Composable
private fun CatalogDarkPreview() {
    CatalogPreviewContent(
        uiState = MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres,
            favoriteIds = setOf(1)
        ),
        darkTheme = true
    )
}

@Preview(
    name = "Catalog - Search",
    showBackground = true
)
@Composable
private fun CatalogSearchPreview() {
    CatalogPreviewContent(
        uiState = MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres,
            searchQuery = "inter"
        )
    )
}