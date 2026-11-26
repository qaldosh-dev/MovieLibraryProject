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
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movielibraryproject.data.local.FavoritesRepository
import com.example.movielibraryproject.data.local.MovieDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.example.movielibraryproject.data.preferences.FavoritesPreferencesRepository
import com.example.movielibraryproject.data.preferences.FavoritesSortOrder
import kotlinx.coroutines.flow.Flow

class MovieViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val sortOrders: Flow<FavoritesSortOrder>,
    private val saveSortOrder: suspend (FavoritesSortOrder) -> Unit
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MovieUiState(
            movies = MovieRepository.movies,
            genres = MovieRepository.genres
        )
    )
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val favoriteWrites = Mutex()

    init {
        viewModelScope.launch {
            favoritesRepository.observeFavorites().collect { favorites ->
                _uiState.update { state ->
                    state.copy(savedFavorites = favorites)
                }
            }
        }
        viewModelScope.launch {
            sortOrders.collect { order ->
                _uiState.update { state ->
                    state.copy(favoriteSortOrder = order)
                }
            }
        }
    }

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
        viewModelScope.launch {
            favoriteWrites.withLock {
                val saved = favoritesRepository.observeFavorite(movieId).first()

                if (saved != null) {
                    favoritesRepository.remove(movieId)
                } else {
                    val movie = uiState.value.movies.firstOrNull { it.id == movieId }
                    movie?.let { favoritesRepository.save(it) }
                }
            }
        }
    }

    fun saveFavoriteNote(movieId: Int, note: String) {
        viewModelScope.launch {
            favoriteWrites.withLock {
                favoritesRepository.updateNote(movieId, note)
            }
        }
    }

    fun setFavoriteSortOrder(order: FavoritesSortOrder) {
        viewModelScope.launch {
            saveSortOrder(order)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val database = MovieDatabase.getInstance(application)
                val preferences = FavoritesPreferencesRepository(application)

                MovieViewModel(
                    favoritesRepository = FavoritesRepository(database.favoriteMovieDao()),
                    sortOrders = preferences.sortOrder,
                    saveSortOrder = preferences::setSortOrder
                )
            }
        }
    }


}
