package com.example.movielibraryproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movielibraryproject.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import com.example.movielibraryproject.model.Movie
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres
        )
    )
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun loadCatalog(fetchMovies: suspend () -> List<Movie>) {
        viewModelScope.launch {
            if (uiState.value.catalogLoadState == CatalogLoadState.Loading) {
                return@launch
            }

            _uiState.update { it.copy(catalogLoadState = CatalogLoadState.Loading) }

            try {
                val movies = fetchMovies()
                _uiState.update { state ->
                    state.copy(
                        movies = movies,
                        catalogLoadState = if (movies.isEmpty()) {
                            CatalogLoadState.Empty
                        } else {
                            CatalogLoadState.Success
                        }
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update { it.copy(catalogLoadState = CatalogLoadState.Error) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(searchQuery = query)
        }
    }

    fun onGenreSelected(genre: String?) {
        _uiState.update { state ->
            if (state.selectedGenre == genre) {
                state
            } else {
                state.copy(selectedGenre = genre, searchQuery = "")
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        _uiState.update { state ->
            if (state.movies.none { it.id == movieId }) {
                state
            } else {
                val updatedIds = if (movieId in state.favoriteIds) {
                    state.favoriteIds - movieId
                } else {
                    state.favoriteIds + movieId
                }
                state.copy(favoriteIds = updatedIds)
            }
        }
    }
}
