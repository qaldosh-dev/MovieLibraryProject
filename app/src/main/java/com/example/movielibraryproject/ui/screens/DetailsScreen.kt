package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.model.Movie
import androidx.compose.ui.tooling.preview.Preview
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.widthIn

@Composable
fun DetailsScreen(
    movie: Movie?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    if (movie == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.details_movie_not_found)
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(movie.posterRes),
            contentDescription = stringResource(
                R.string.details_poster_description,
                movie.title
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 240.dp)
                .fillMaxWidth()
                .aspectRatio(2f / 3f),
            contentScale = ContentScale.Fit
        )

        Text(
            text = movie.genre,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = stringResource(
                R.string.details_year,
                movie.year
            ),
            style = MaterialTheme.typography.bodyMedium
        )


        Text(
            text = stringResource(
                R.string.details_rating,
                movie.rating
            ),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(
                    if (isFavorite) {
                        R.string.details_remove_favorite
                    } else {
                        R.string.details_add_favorite
                    }
                )
            )
        }
    }
}

private val previewMovie = Movie(
    id = 1,
    title = "Interstellar",
    year = 2014,
    genre = "Science Fiction",
    rating = 9.0,
    description = "A pilot travels to distant stars to find a new home for humanity.",
    posterRes = R.drawable.poster_interstellar
)

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {
    MovieLibraryProjectTheme {
        DetailsScreen(
            movie = previewMovie,
            isFavorite = false,
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenFavoritePreview() {
    MovieLibraryProjectTheme {
        DetailsScreen(
            movie = previewMovie,
            isFavorite = true,
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenNotFoundPreview() {
    MovieLibraryProjectTheme {
        DetailsScreen(
            movie = null,
            isFavorite = false,
            onFavoriteClick = {}
        )
    }
}

