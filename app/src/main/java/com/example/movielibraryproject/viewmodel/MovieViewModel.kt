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
import com.example.movielibraryproject.data.local.FavoriteMovie

class MovieViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres
        )
    )
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun loadCatalog(
        fetchMovies: suspend () -> List<Movie>,
        getGenres: (List<Movie>) -> List<String>
    ) {
        viewModelScope.launch {
            if (uiState.value.catalogLoadState == CatalogLoadState.Loading) {
                return@launch
            }

            _uiState.update { it.copy(catalogLoadState = CatalogLoadState.Loading) }

            try {
                val movies = fetchMovies()
                val genres = getGenres(movies)
                _uiState.update { state ->
                    state.copy(
                        movies = movies,
                        genres = genres,
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
            val updatedFavorites = if (movieId in state.favoriteIds) {
                state.savedFavorites.filterNot { it.movie.id == movieId }
            } else {
                val movie = state.movies.find { it.id == movieId } ?: return@update state

                (state.savedFavorites + FavoriteMovie(movie, note = ""))
                    .sortedBy { favorite ->
                        state.movies.indexOfFirst { it.id == favorite.movie.id }
                    }
            }

            state.copy(savedFavorites = updatedFavorites)
        }
    }
}
