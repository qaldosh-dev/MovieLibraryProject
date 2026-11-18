package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movielibraryproject.R
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme

@Composable
fun GenresScreen(
    genres: List<String>,
    onGenreClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(genres) { genre ->
            GenreCard(genre = genre, onClick = { onGenreClick(genre) })
        }
    }
}

@Composable
private fun GenreCard(genre: String, onClick: () -> Unit) {
    val posterRes = when (genre) {
        "Science Fiction" -> R.drawable.poster_interstellar
        "Drama" -> R.drawable.poster_the_truman_show
        "Comedy" -> R.drawable.poster_paddington
        else -> null
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.BottomStart
        ) {
            if (posterRes != null) {
                Image(
                    painter = painterResource(posterRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
            Text(
                text = genre,
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
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
