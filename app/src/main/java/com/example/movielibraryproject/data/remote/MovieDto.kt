package com.example.movielibraryproject.data.remote

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("poster_path") val posterPath: String? = null
)
