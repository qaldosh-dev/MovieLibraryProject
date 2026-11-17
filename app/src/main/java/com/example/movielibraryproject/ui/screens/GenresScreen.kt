package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme

@Composable
fun GenresScreen(
    genres: List<String>,
    onGenreClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(genres) { genre ->
            Card(
                onClick = { onGenreClick(genre) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = genre,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GenresScreenPreview() {
    MovieLibraryProjectTheme {
        Surface {
            GenresScreen(
                genres = listOf("Science Fiction", "Drama", "Comedy"),
                onGenreClick = {}
            )
        }
    }
}
