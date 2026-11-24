package com.example.movielibraryproject.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val description: String,
    val posterRes: Int = 0,
    val releaseDate: String? = null,
    val genreIds: List<Int> = emptyList(),
    val genres: List<String> = listOf(genre).filter { it.isNotBlank() },
    val posterUrl: String? = null
)
