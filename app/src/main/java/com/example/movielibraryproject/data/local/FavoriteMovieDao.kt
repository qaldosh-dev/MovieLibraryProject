package com.example.movielibraryproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: FavoriteMovieEntity): Long

    @Query("SELECT * FROM favorite_movies ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    fun observeById(id: Int): Flow<FavoriteMovieEntity?>

    @Query("UPDATE favorite_movies SET note = :note WHERE id = :id")
    suspend fun updateNote(id: Int, note: String): Int

    @Query("DELETE FROM favorite_movies WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}