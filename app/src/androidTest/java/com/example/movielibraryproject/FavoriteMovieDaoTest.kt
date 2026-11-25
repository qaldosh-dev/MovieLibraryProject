
package com.example.movielibraryproject

import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withTimeout
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
import org.junit.Assert.assertNull

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
    @Test
    fun updateNote_changesOnlyNote() = runBlocking {
        dao.insert(movie(1))

        val updated = dao.updateNote(1, "new note")

        assertEquals(1, updated)
        assertEquals("new note", dao.observeById(1).first()?.note)
    }

    @Test
    fun deleteById_removesMovie_andFlowReflectsIt() = runBlocking {
        dao.insert(movie(1))
        dao.insert(movie(2))

        val deleted = dao.deleteById(1)

        assertEquals(1, deleted)
        assertNull(dao.observeById(1).first())
        assertEquals(listOf(2), dao.observeAll().first().map { it.id })
    }

    @Test
    fun flow_emitsChangesAutomatically() = runBlocking {
        withTimeout(10000) {
            val updates = Channel<List<FavoriteMovieEntity>>(
                Channel.UNLIMITED
            )

            val job = launch {
                dao.observeAll().collect {
                    updates.send(it)
                }
            }

            assertEquals(0, updates.receive().size)

            dao.insert(movie(1))
            assertEquals(1, updates.receive().size)

            dao.updateNote(1, "Updated note")
            assertEquals("Updated note", updates.receive()[0].note)

            dao.deleteById(1)
            assertEquals(0, updates.receive().size)

            job.cancel()
        }
    }
}
