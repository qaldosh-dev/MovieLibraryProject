package com.example.movielibraryproject.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movielibraryproject.data.local.FavoriteMovieDao
import com.example.movielibraryproject.data.local.FavoriteMovieEntity
import com.example.movielibraryproject.data.local.FavoritesRepository
import com.example.movielibraryproject.data.MovieRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {

    private lateinit var viewModel: MovieViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MovieViewModel(FavoritesRepository(TestFavoriteMovieDao()))
    }

    @After
    fun tearDown() {
        viewModel.viewModelScope.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateShowsAllMoviesAndNoFavorites() {
        val state = viewModel.uiState.value

        assertEquals(MovieRepository.movies, state.movies)
        assertEquals(MovieRepository.genres, state.genres)
        assertEquals(state.movies, state.filteredMovies)
        assertTrue(state.favoriteMovies.isEmpty())
    }

    @Test
    fun searchIgnoresCaseAndSurroundingSpaces() {
        viewModel.onSearchQueryChange("  iNtEr  ")

        assertEquals(listOf(1), viewModel.uiState.value.filteredMovies.map { it.id })
        assertEquals("  iNtEr  ", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun blankSearchShowsAllMoviesAndUnknownTitleShowsNone() {
        viewModel.onSearchQueryChange("not a movie title")
        assertTrue(viewModel.uiState.value.filteredMovies.isEmpty())

        viewModel.onSearchQueryChange("   ")

        assertEquals(MovieRepository.movies, viewModel.uiState.value.filteredMovies)
    }

    @Test
    fun searchAndGenreApplyTogether() {
        viewModel.onGenreSelected("Science Fiction")
        assertEquals(listOf(1, 2), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onSearchQueryChange("martian")
        assertEquals(listOf(2), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onSearchQueryChange("home")
        assertTrue(viewModel.uiState.value.filteredMovies.isEmpty())
    }

    @Test
    fun selectingSameGenrePreservesSearch() {
        viewModel.onGenreSelected("Comedy")
        viewModel.onSearchQueryChange("home")

        viewModel.onGenreSelected("Comedy")

        assertEquals("home", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(6), viewModel.uiState.value.filteredMovies.map { it.id })
    }

    @Test
    fun changingGenreClearsPreviousSearchAndNullRestoresAllGenres() {
        viewModel.onSearchQueryChange("interstellar")

        viewModel.onGenreSelected("Comedy")
        assertEquals("", viewModel.uiState.value.searchQuery)
        assertEquals(listOf(5, 6), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.onGenreSelected(null)
        assertEquals(MovieRepository.movies, viewModel.uiState.value.filteredMovies)
    }

    @Test
    fun favoritesSurviveFilteringAndCanBeRemoved() {
        viewModel.toggleFavorite(1)
        viewModel.toggleFavorite(6)
        viewModel.onGenreSelected("Drama")
        viewModel.onSearchQueryChange("truman")

        assertEquals(setOf(1, 6), viewModel.uiState.value.favoriteIds)
        assertEquals(setOf(1, 6), viewModel.uiState.value.favoriteMovies.map { it.id }.toSet())
        assertEquals(listOf(3), viewModel.uiState.value.filteredMovies.map { it.id })

        viewModel.toggleFavorite(1)
        assertEquals(setOf(6), viewModel.uiState.value.favoriteIds)
        viewModel.toggleFavorite(6)
        assertTrue(viewModel.uiState.value.favoriteMovies.isEmpty())
        assertEquals("truman", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun unknownMovieIdDoesNotChangeFavorites() {
        viewModel.toggleFavorite(1)

        viewModel.toggleFavorite(-1)

        assertEquals(setOf(1), viewModel.uiState.value.favoriteIds)
    }
}

private class TestFavoriteMovieDao : FavoriteMovieDao {
    private val records = MutableStateFlow<List<FavoriteMovieEntity>>(emptyList())

    override fun observeAll() = records

    override fun observeById(id: Int) =
        records.map { movies -> movies.firstOrNull { it.id == id } }

    override suspend fun insert(movie: FavoriteMovieEntity): Long {
        if (records.value.any { it.id == movie.id }) return -1L
        records.update { listOf(movie) + it }
        return movie.id.toLong()
    }

    override suspend fun updateNote(id: Int, note: String): Int {
        if (records.value.none { it.id == id }) return 0
        records.update { movies ->
            movies.map { if (it.id == id) it.copy(note = note) else it }
        }
        return 1
    }

    override suspend fun deleteById(id: Int): Int {
        if (records.value.none { it.id == id }) return 0
        records.update { movies -> movies.filterNot { it.id == id } }
        return 1
    }
}
