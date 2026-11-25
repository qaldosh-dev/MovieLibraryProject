
package com.example.movielibraryproject.data.local

import com.example.movielibraryproject.model.Movie

const val GENRE_SEPARATOR = "|"

data class FavoriteMovie(
    val movie: Movie,
    val note: String
)

fun Movie.toEntity(note: String = ""): FavoriteMovieEntity =
    FavoriteMovieEntity(
        id = id,
        title = title,
        overview = description,
        posterPath = posterUrl,
        releaseYear = releaseDate?.take(4)?.toIntOrNull() ?: year,
        genres = genres.joinToString(GENRE_SEPARATOR),
        rating = rating,
        note = note
    )

fun FavoriteMovieEntity.toMovie(): Movie {
    val genreList = genres.split(GENRE_SEPARATOR)
        .filter { it.isNotBlank() }

    return Movie(
        id = id,
        title = title,
        year = releaseYear ?: 0,
        genre = genreList.firstOrNull() ?: "",
        rating = rating,
        description = overview,
        releaseDate = releaseYear?.toString(),
        genres = genreList,
        posterUrl = posterPath
    )
}
