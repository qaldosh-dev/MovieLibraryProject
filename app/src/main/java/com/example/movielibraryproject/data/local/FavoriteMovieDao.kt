package com.example.movielibraryproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: FavoriteMovieEntity): Long
}