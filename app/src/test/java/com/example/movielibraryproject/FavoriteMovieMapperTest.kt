package com.example.movielibraryproject

import com.example.movielibraryproject.data.local.toEntity
import com.example.movielibraryproject.data.local.toMovie
import com.example.movielibraryproject.data.local.toFavoriteMovie
import com.example.movielibraryproject.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteMovieMapperTest {

    private val movie = Movie(
        id = 1,
        title = "Interstellar",
        year = 2014,
        genre = "Drama",
        rating = 8.7,
        description = "Space movie",
        releaseDate = "2014-11-07",
        genres = listOf("Drama", "Science Fiction"),
        posterUrl = "/poster.jpg"
    )

    @Test
    fun movie_toEntity_mapsFieldsCorrectly() {
        val entity = movie.toEntity("My favorite")

        assertEquals(movie.id, entity.id)
        assertEquals(movie.title, entity.title)
        assertEquals(movie.description, entity.overview)
        assertEquals(movie.posterUrl, entity.posterPath)
        assertEquals(2014, entity.releaseYear)
        assertEquals("Drama|Science Fiction", entity.genres)
        assertEquals(movie.rating, entity.rating, 0.0)
        assertEquals("My favorite", entity.note)
    }

    @Test
    fun entity_toMovie_mapsFieldsCorrectly() {
        val result = movie.toEntity().toMovie()

        assertEquals(movie.id, result.id)
        assertEquals(movie.title, result.title)
        assertEquals(movie.description, result.description)
        assertEquals(movie.posterUrl, result.posterUrl)
        assertEquals(movie.year, result.year)
        assertEquals(movie.genres, result.genres)
        assertEquals(movie.genre, result.genre)
        assertEquals(movie.rating, result.rating, 0.0)
    }

    @Test
    fun entity_toFavoriteMovie_preservesNote() {
        val result = movie.toEntity("My note").toFavoriteMovie()

        assertEquals(movie.id, result.movie.id)
        assertEquals("My note", result.note)
    }
}