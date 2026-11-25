package com.example.movielibraryproject.data.remote

import com.example.movielibraryproject.model.Movie
import java.io.IOException

class RemoteMovieRepository(
    private val api: MovieApi = TmdbClient.retrofit.create(MovieApi::class.java),
    private val mapper: MovieMapper = MovieMapper()
) {
    suspend fun getMovies(page: Int = 1): List<Movie> {
        require(page in 1..500) { "TMDB page must be between 1 and 500." }
        val response = api.discoverMovies(page = page, language = "en-US")
        val results = response.results
            ?: throw IOException("TMDB returned an invalid movie response: results is missing.")
        return results.map { mapper.toMovie(it) }
    }

    fun getGenres(movies: List<Movie>): List<String> = mapper.getGenres(movies)
}
