package com.example.movielibraryproject.data

import com.example.movielibraryproject.R
import com.example.movielibraryproject.model.Movie

object MovieRepository {
    val movies: List<Movie> = listOf(
        Movie(
            id = 1,
            title = "Interstellar",
            year = 2014,
            genre = "Science Fiction",
            rating = 9.0,
            description = "Pilot Cooper travels to distant stars to find a new home for humanity. " +
                "While he explores space, his daughter searches for a way to save the people on Earth.",
            posterRes = R.drawable.poster_interstellar
        ),
        Movie(
            id = 2,
            title = "The Martian",
            year = 2015,
            genre = "Science Fiction",
            rating = 8.5,
            description = "After an accident, astronaut Mark Watney is stranded alone on Mars. " +
                "He uses science and creativity to survive and contact Earth.",
            posterRes = R.drawable.poster_the_martian
        ),
        Movie(
            id = 3,
            title = "The Truman Show",
            year = 1998,
            genre = "Drama",
            rating = 8.5,
            description = "Truman lives in a quiet town but begins to notice strange things around him. " +
                "His search for the truth makes him question the world he has always known.",
            posterRes = R.drawable.poster_the_truman_show
        ),
        Movie(
            id = 4,
            title = "The Pursuit of Happyness",
            year = 2006,
            genre = "Drama",
            rating = 8.0,
            description = "Chris Gardner struggles to escape poverty and build a better future for his young son. " +
                "A demanding internship gives him a chance to start a new career.",
            posterRes = R.drawable.poster_the_pursuit_of_happyness
        ),
        Movie(
            id = 5,
            title = "Paddington",
            year = 2014,
            genre = "Comedy",
            rating = 8.0,
            description = "A polite bear from Peru arrives in London looking for a home. " +
                "The Brown family takes him in, and life in the big city becomes an adventure.",
            posterRes = R.drawable.poster_paddington
        ),
        Movie(
            id = 6,
            title = "Home Alone",
            year = 1990,
            genre = "Comedy",
            rating = 8.5,
            description = "Kevin is accidentally left at home when his family goes away for the holidays. " +
                "When two burglars target the house, he prepares clever traps to defend it.",
            posterRes = R.drawable.poster_home_alone
        )
    )
    val genres: List<String> = movies.map { it.genre }.distinct()
    fun getMovieById(movieId: Int): Movie? {
        return movies.find { it.id == movieId }
    }
}
