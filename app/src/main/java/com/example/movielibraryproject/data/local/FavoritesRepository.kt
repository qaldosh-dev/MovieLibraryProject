
package com.example.movielibraryproject.data.local

import com.example.movielibraryproject.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val dao: FavoriteMovieDao
) {

    fun observeFavorites(): Flow<List<FavoriteMovie>> =
        dao.observeAll().map { list ->
            list.map { it.toFavoriteMovie() }
        }

    fun observeFavorite(id: Int): Flow<FavoriteMovie?> =
        dao.observeById(id).map { entity ->
            entity?.toFavoriteMovie()
        }

    suspend fun save(movie: Movie): Boolean =
        dao.insert(movie.toEntity()) != -1L
}
