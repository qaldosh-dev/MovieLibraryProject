package com.example.movielibraryproject.viewmodel

import com.example.movielibraryproject.model.Movie

data class MovieUiState(
    val movies: List<Movie> = emptyList(),
    val genres: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedGenre: String? = null,
    val favoriteIds: Set<Int> = emptySet()
) {
    val filteredMovies: List<Movie>
        get() = movies.filter { movie ->
            val matchesQuery = movie.title.contains(searchQuery.trim(), ignoreCase = true)
            val matchesGenre = selectedGenre == null || movie.genre == selectedGenre
            matchesQuery && matchesGenre
        }

    val favoriteMovies: List<Movie>
        get() = movies.filter { it.id in favoriteIds }
}
