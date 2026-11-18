package com.example.movielibraryproject.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
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
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.genres_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = stringResource(R.string.genres_intro),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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

    val subtitleRes = when (genre) {
        "Science Fiction" -> R.string.genres_science_fiction_subtitle
        "Drama" -> R.string.genres_drama_subtitle
        "Comedy" -> R.string.genres_comedy_subtitle
        else -> R.string.genres_other_subtitle
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
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = genre,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    text = stringResource(subtitleRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun GenresPreviewContent(
    darkTheme: Boolean,
    genres: List<String> = listOf("Science Fiction", "Drama", "Comedy")
) {
    MovieLibraryProjectTheme(darkTheme = darkTheme, dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            GenresScreen(genres = genres, onGenreClick = {})
        }
    }
}

@Preview(name = "Genres light", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun GenresLightPreview() {
    GenresPreviewContent(darkTheme = false)
}

@Preview(name = "Genres dark", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun GenresDarkPreview() {
    GenresPreviewContent(darkTheme = true)
}

@Preview(
    name = "Genres compact large font",
    showBackground = true,
    widthDp = 320,
    heightDp = 480,
    fontScale = 1.5f
)
@Composable
private fun GenresLargeFontPreview() {
    GenresPreviewContent(darkTheme = false)
}

@Preview(
    name = "Genres landscape large font",
    showBackground = true,
    widthDp = 640,
    heightDp = 360,
    fontScale = 1.5f
)
@Composable
private fun GenresLandscapePreview() {
    GenresPreviewContent(darkTheme = true)
}

@Preview(
    name = "Unknown genre",
    showBackground = true,
    widthDp = 320,
    heightDp = 480,
    fontScale = 1.5f
)
@Composable
private fun GenresUnknownPreview() {
    GenresPreviewContent(
        darkTheme = false,
        genres = listOf("Documentary and Independent Cinema")
    )
}

@Preview(name = "Empty genres light", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun GenresEmptyLightPreview() {
    GenresPreviewContent(darkTheme = false, genres = emptyList())
}

@Preview(name = "Empty genres dark", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun GenresEmptyDarkPreview() {
    GenresPreviewContent(darkTheme = true, genres = emptyList())
}
