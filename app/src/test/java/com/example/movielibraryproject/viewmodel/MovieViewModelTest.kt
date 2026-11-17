package com.example.movielibraryproject.viewmodel

import com.example.movielibraryproject.data.MovieRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieViewModelTest {
    @Test
    fun initialStateShowsAllMoviesAndNoFavorites() {
        val state = MovieViewModel().uiState.value

        assertEquals(MovieRepository.movies, state.movies)
        assertEquals(MovieRepository.genres, state.genres)
        assertEquals(state.movies, state.filteredMovies)
        assertTrue(state.favoriteMovies.isEmpty())
    }

    @Test
    fun searchIgnoresCaseAndSurroundingSpaces() {
        val viewModel = MovieViewModel()

        viewModel.onSearchQueryChange("  iNtEr  ")

        assertEquals(listOf(1), viewModel.uiState.value.filteredMovies.map { it.id })
        assertEquals("  iNtEr  ", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun blankSearchShowsAllMoviesAndUnknownTitleShowsNone() {
        val viewModel = MovieViewModel()
        viewModel.onSearchQueryChange("not a movie title")
        assertTrue(viewModel.uiState.value.filteredMovies.isEmpty())

        viewModel.onSearchQueryChange("   ")

        assertEquals(MovieRepository.movies, viewModel.uiState.value.filteredMovies)
    }

    @Test
    fun searchAndGenreApplyTogether() {
        val viewModel = MovieViewModel()
        viewModel.onGenreSelected("Science Fiction")
        assertEquals(listOf(1, 2), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onSearchQueryChange("martian")
        assertEquals(listOf(2), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onSearchQueryChange("home")
        assertTrue(viewModel.uiState.value.filteredMovies.isEmpty())
    }

    @Test
    fun selectingSameGenrePreservesSearch() {
        val viewModel = MovieViewModel()
        viewModel.onGenreSelected("Comedy")
        viewModel.onSearchQueryChange("home")

        viewModel.onGenreSelected("Comedy")

        assertEquals("home", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(6), viewModel.uiState.value.filteredMovies.map { it.id })
    }

    @Test
    fun changingGenreClearsPreviousSearchAndNullRestoresAllGenres() {
        val viewModel = MovieViewModel()
        viewModel.onSearchQueryChange("interstellar")

        viewModel.onGenreSelected("Comedy")
        assertEquals("", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(5, 6), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onGenreSelected(null)
        assertEquals(MovieRepository.movies, viewModel.uiState.value.filteredMovies)
    }

    @Test
    fun favoritesSurviveFilteringAndCanBeRemoved() {
        val viewModel = MovieViewModel()
        viewModel.toggleFavorite(1)
        viewModel.toggleFavorite(6)
        viewModel.onGenreSelected("Drama")
        viewModel.onSearchQueryChange("truman")

        assertEquals(setOf(1, 6), viewModel.uiState.value.favoriteIds)
        assertEquals(listOf(1, 6), viewModel.uiState.value.favoriteMovies.map { it.id })
        assertEquals(listOf(3), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.toggleFavorite(1)
        assertEquals(setOf(6), viewModel.uiState.value.favoriteIds)
        viewModel.toggleFavorite(6)
        assertTrue(viewModel.uiState.value.favoriteMovies.isEmpty())
        assertEquals("truman", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun unknownMovieIdDoesNotChangeFavorites() {
        val viewModel = MovieViewModel()
        viewModel.toggleFavorite(1)

        viewModel.toggleFavorite(-1)

        assertEquals(setOf(1), viewModel.uiState.value.favoriteIds)
    }
}
