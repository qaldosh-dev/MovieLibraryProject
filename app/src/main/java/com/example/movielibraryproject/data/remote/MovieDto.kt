package com.example.movielibraryproject.data.remote

import com.example.movielibraryproject.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("poster_path") val posterPath: String? = null
) {
    fun toMovie(genres: List<String> = emptyList()): Movie {
        val tmdbId = requireNotNull(id) { "TMDB movie response is missing its ID." }
        require(tmdbId > 0) { "TMDB movie ID must be positive." }
        val date = releaseDate?.takeIf { it.isNotBlank() }
        val year = date?.take(4)?.toIntOrNull()?.takeIf { it > 0 } ?: 0
        val poster = posterPath?.takeIf { it.isNotBlank() }

        return Movie(
            id = tmdbId,
            title = title.orEmpty(),
            year = year,
            genre = genres.firstOrNull().orEmpty(),
            rating = voteAverage ?: 0.0,
            description = overview.orEmpty(),
            releaseDate = date,
            genreIds = genreIds.orEmpty(),
            genres = genres,
            posterUrl = poster?.let { "https://image.tmdb.org/t/p/w500/${it.trimStart('/')}" }
        )
    }
}
