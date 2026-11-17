package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.model.Movie

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
            Text(text = stringResource(R.string.details_movie_not_found))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = movie.year.toString(),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = movie.genre,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = movie.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}