package com.example.movielibraryproject.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseYear: Int?,
    val genres: String,
    val rating: Double,
    val note: String = "",
    val addedAt: Long = System.currentTimeMillis()
)