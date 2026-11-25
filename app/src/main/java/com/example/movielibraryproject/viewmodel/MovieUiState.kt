package com.example.movielibraryproject.viewmodel

import com.example.movielibraryproject.data.local.FavoriteMovie
import com.example.movielibraryproject.model.Movie

data class MovieUiState(
    val movies: List<Movie> = emptyList(),
    val genres: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedGenre: String? = null,
    val savedFavorites: List<FavoriteMovie> = emptyList(),
    val catalogLoadState: CatalogLoadState = CatalogLoadState.Success
) {
    val filteredMovies: List<Movie>
        get() = movies.filter { movie ->
            val matchesQuery = movie.title.contains(searchQuery.trim(), ignoreCase = true)
            val matchesGenre = selectedGenre == null || selectedGenre in movie.genres
            matchesQuery && matchesGenre
        }

    val favoriteIds: Set<Int>
        get() = savedFavorites.map { it.movie.id }.toSet()

    val favoriteMovies: List<Movie>
        get() = savedFavorites.map { it.movie }
}