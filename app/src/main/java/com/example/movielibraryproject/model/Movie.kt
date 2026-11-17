package com.example.movielibraryproject.model

data class Movie(
    val id: Int,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val description: String,
    val posterRes: Int
)
