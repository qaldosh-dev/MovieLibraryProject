package com.example.movielibraryproject

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.movielibraryproject.data.local.FavoriteMovieEntity
import com.example.movielibraryproject.data.local.MovieDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteMoviePersistenceTest {

    private lateinit var context: Context
    private lateinit var database: MovieDatabase
    private val databaseName = "persistence_test.db"

    private fun openDatabase(): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            databaseName
        ).build()
    }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(databaseName)
        database = openDatabase()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    private fun movie(id: Int) = FavoriteMovieEntity(
        id = id,
        title = "Movie $id",
        overview = "Description",
        posterPath = null,
        releaseYear = 2025,
        genres = "Drama|Thriller",
        rating = 7.5
    )

    @Test
    fun movieSurvivesDatabaseReopen() = runBlocking {
        database.favoriteMovieDao().insert(movie(1))

        database.close()
        database = openDatabase()

        val saved = database.favoriteMovieDao()
            .observeById(1).first()

        assertEquals(1, saved?.id)
        assertEquals("Movie 1", saved?.title)
    }
    @Test
    fun updatedNoteSurvivesDatabaseReopen() = runBlocking {
        database.favoriteMovieDao().insert(movie(1))

        database.favoriteMovieDao().updateNote(1, "Amazing movie!")

        database.close()
        database = openDatabase()

        val saved = database.favoriteMovieDao()
            .observeById(1).first()

        assertEquals(1, saved?.id)
        assertEquals("Amazing movie!", saved?.note)
    }
}
