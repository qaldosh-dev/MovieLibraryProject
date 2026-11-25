
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteMovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var dao: com.example.movielibraryproject.data.local.FavoriteMovieDao

    private fun movie(
        id: Int,
        title: String = "Movie $id"
    ) = FavoriteMovieEntity(
        id = id,
        title = title,
        overview = "Description",
        posterPath = null,
        releaseYear = 2025,
        genres = "Drama|Thriller",
        rating = 7.5
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            MovieDatabase::class.java
        ).build()

        dao = database.favoriteMovieDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert_savesMovie() = runBlocking {
        dao.insert(movie(1))

        val all = dao.observeAll().first()

        assertEquals(1, all.size)
        assertEquals(1, all[0].id)
    }

    @Test
    fun insert_sameIdTwice_doesNotDuplicateOrLoseNote() = runBlocking {
        dao.insert(movie(1))

        dao.updateNote(1, "my note")

        dao.insert(movie(1, title = "Changed"))

        val all = dao.observeAll().first()

        assertEquals(1, all.size)
        assertEquals("my note", all[0].note)
        assertEquals("Movie 1", all[0].title)
    }
}
