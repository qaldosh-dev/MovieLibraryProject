package com.example.movielibraryproject.ui.navigation

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalUriHandler
import com.example.movielibraryproject.model.Movie
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movielibraryproject.R
import com.example.movielibraryproject.ui.screens.CatalogScreen
import com.example.movielibraryproject.ui.screens.DetailsScreen
import com.example.movielibraryproject.ui.screens.FavoritesScreen
import com.example.movielibraryproject.ui.screens.GenresScreen
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme
import com.example.movielibraryproject.viewmodel.MovieViewModel
import androidx.compose.runtime.remember
import com.example.movielibraryproject.data.remote.RemoteMovieRepository

private const val CatalogRoute = "catalog?genre={genre}"
private const val GenresRoute = "genres"
private const val FavoritesRoute = "favorites"
private const val DetailsRoute = "details/{movieId}?fromFavorites={fromFavorites}"

@Composable
fun MovieLibraryApp() {
    val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
    val uiState by movieViewModel.uiState.collectAsState()
    var showCredits by rememberSaveable { mutableStateOf(false) }

    val remoteRepository = remember { RemoteMovieRepository() }

    fun requestCatalog() {
        movieViewModel.loadCatalog(
            fetchMovies = { remoteRepository.getMovies() },
            getGenres = remoteRepository::getGenres
        )
    }

    LaunchedEffect(movieViewModel) {
        requestCatalog()
    }

    val navController = rememberNavController()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route
    val currentGenre = currentEntry?.arguments?.getString("genre")
    val isDetails = currentRoute == DetailsRoute
    val isFilteredCatalog = currentRoute == CatalogRoute && currentGenre != null
    val title = stringResource(
        when (currentRoute) {
            GenresRoute -> R.string.navigation_genres
            FavoritesRoute -> R.string.navigation_favorites
            DetailsRoute -> R.string.navigation_details
            else -> R.string.navigation_catalog
        }
    )
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MovieTopBar(
                title = title,
                showBack = isDetails || isFilteredCatalog,
                onBackClick = { navController.popBackStack() },
                onAboutClick = { showCredits = true }
            )
        },
        bottomBar = {
            if (!isDetails) {
                MovieBottomBar(
                    currentRoute = currentRoute,
                    onSectionClick = { navController.openMainSection(it) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CatalogRoute,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(
                route = CatalogRoute,
                arguments = listOf(
                    navArgument("genre") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { entry ->
                val genre = entry.arguments?.getString("genre")
                LaunchedEffect(entry, genre) {
                    movieViewModel.onGenreSelected(genre)
                }
                CatalogScreen(
                    loadState = uiState.catalogLoadState,
                    onRetry = ::requestCatalog,
                    uiState = uiState.copy(selectedGenre = genre),
                    onQueryChange = movieViewModel::onSearchQueryChange,
                    onMovieClick = { movieId ->
                        navController.openMovieDetails(movieId)
                    },
                    onFavoriteClick = movieViewModel::toggleFavorite,
                    onClearGenre = {
                        navController.openMainSection("catalog")
                    }
                )
            }
            composable(GenresRoute) {
                GenresScreen(
                    genres = uiState.genres,
                    onGenreClick = { genre ->
                        navController.navigate("catalog?genre=${Uri.encode(genre)}") {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(FavoritesRoute) {
                FavoritesScreen(
                    movies = uiState.favoriteMovies,
                    onMovieClick = { movieId ->
                        navController.openMovieDetails(movieId, fromFavorites = true)
                    },
                    onFavoriteClick = movieViewModel::toggleFavorite
                )
            }
            composable(
                route = DetailsRoute,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("fromFavorites") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { entry ->
                val movieId = entry.arguments?.getInt("movieId")
                val savedFavorite = uiState.savedFavorites.firstOrNull { it.movie.id == movieId }
                val movie = resolveDetailsMovie(
                    movieId = movieId,
                    movies = uiState.movies,
                    favoriteMovies = uiState.favoriteMovies,
                    fromFavorites = entry.arguments?.getBoolean("fromFavorites") == true
                )
                DetailsScreen(
                    movie = movie,
                    isFavorite = movie != null && movie.id in uiState.favoriteIds,
                    onFavoriteClick = {
                        movie?.let { movieViewModel.toggleFavorite(it.id) }
                    },
                    note = savedFavorite?.note.orEmpty(),
                    onSaveNote = { note ->
                        savedFavorite?.let { movieViewModel.saveFavoriteNote(it.movie.id, note) }
                    }
                )
            }
        }
    }
    if (showCredits) {
        TmdbCreditsDialog(onDismiss = { showCredits = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieTopBar(
    title: String,
    showBack: Boolean,
    onBackClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    TopAppBar(
        windowInsets = TopAppBarDefaults.windowInsets,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_brand_film),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = stringResource(R.string.navigation_app_title),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_nav_back),
                        contentDescription = stringResource(R.string.navigation_back)
                    )
                }
            }
        },
        actions = {
            TextButton(
                onClick = onAboutClick,
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            ) {
                Text(stringResource(R.string.navigation_about))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun MovieBottomBar(
    currentRoute: String?,
    onSectionClick: (String) -> Unit
) {
    val destinations = listOf(
        Triple("catalog", R.string.navigation_catalog, R.drawable.ic_nav_catalog),
        Triple(GenresRoute, R.string.navigation_genres, R.drawable.ic_nav_genres),
        Triple(FavoritesRoute, R.string.navigation_favorites, R.drawable.ic_nav_favorites)
    )
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp
    ) {
        destinations.forEach { (route, labelRes, iconRes) ->
            val label = stringResource(labelRes)
            val selected = if (route == "catalog") {
                currentRoute == CatalogRoute
            } else {
                currentRoute == route
            }
            NavigationBarItem(
                selected = selected,
                onClick = { onSectionClick(route) },
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp),
                icon = {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

internal fun resolveDetailsMovie(
    movieId: Int?,
    movies: List<Movie>,
    favoriteMovies: List<Movie>,
    fromFavorites: Boolean
): Movie? {
    if (movieId == null) return null
    val catalogMovie = movies.firstOrNull { it.id == movieId }
    val savedMovie = favoriteMovies.firstOrNull { it.id == movieId }
    return if (fromFavorites) savedMovie ?: catalogMovie else catalogMovie ?: savedMovie
}

internal fun movieDetailsRoute(movieId: Int, fromFavorites: Boolean = false): String =
    "details/$movieId?fromFavorites=$fromFavorites"

private fun NavHostController.openMovieDetails(movieId: Int, fromFavorites: Boolean = false) {
    navigate(movieDetailsRoute(movieId, fromFavorites)) {
        launchSingleTop = true
    }
}

@Composable
private fun TmdbCreditsDialog(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val tmdbUrl = stringResource(R.string.navigation_tmdb_url)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.navigation_about_title)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    imageVector = TmdbLogo,
                    contentDescription = stringResource(R.string.navigation_tmdb_logo),
                    modifier = Modifier.width(144.dp).aspectRatio(273.42f / 35.52f)
                )
                Text(stringResource(R.string.navigation_tmdb_attribution))
                TextButton(onClick = { uriHandler.openUri(tmdbUrl) }) {
                    Text(stringResource(R.string.navigation_tmdb_website))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.navigation_close))
            }
        }
    )
}

private fun NavHostController.openMainSection(route: String) {
    navigate("catalog") {
        popUpTo(graph.id) {
            inclusive = false
        }
        launchSingleTop = true
    }
    if (route != "catalog") {
        navigate(route) {
            launchSingleTop = true
        }
    }
}

private val TmdbLogo: ImageVector by lazy {
    ImageVector.Builder(
        name = "TMDB",
        defaultWidth = 273.42.dp,
        defaultHeight = 35.52.dp,
        viewportWidth = 273.42f,
        viewportHeight = 35.52f
    ).addPath(
        pathData = PathParser().parsePathString(TmdbLogoPath).toNodes(),
        fill = Brush.linearGradient(
            0f to Color(0xFF90CEA1),
            0.56f to Color(0xFF3CBEC9),
            1f to Color(0xFF00B3E5),
            start = Offset(0f, 17.76f),
            end = Offset(273.42f, 17.76f)
        )
    ).build()
}

private const val TmdbLogoPath = """M191.85,35.37h63.9A17.67,17.67,0,0,0,273.42,17.7h0A17.67,17.67,0,0,0,255.75,0h-63.9A17.67,17.67,0,0,0,174.18,17.7h0A17.67,17.67,0,0,0,191.85,35.37ZM10.1,35.42h7.8V6.92H28V0H0v6.9H10.1Zm28.1,0H46V8.25h.1L55.05,35.4h6L70.3,8.25h.1V35.4h7.8V0H66.45l-8.2,23.1h-.1L50,0H38.2ZM89.14.12h11.7a33.56,33.56,0,0,1,8.08,1,18.52,18.52,0,0,1,6.67,3.08,15.09,15.09,0,0,1,4.53,5.52,18.5,18.5,0,0,1,1.67,8.25,16.91,16.91,0,0,1-1.62,7.58,16.3,16.3,0,0,1-4.38,5.5,19.24,19.24,0,0,1-6.35,3.37,24.53,24.53,0,0,1-7.55,1.15H89.14Zm7.8,28.2h4a21.66,21.66,0,0,0,5-.55A10.58,10.58,0,0,0,110,26a8.73,8.73,0,0,0,2.68-3.35,11.9,11.9,0,0,0,1-5.08,9.87,9.87,0,0,0-1-4.52,9.17,9.17,0,0,0-2.63-3.18A11.61,11.61,0,0,0,106.22,8a17.06,17.06,0,0,0-4.68-.63h-4.6ZM133.09.12h13.2a32.87,32.87,0,0,1,4.63.33,12.66,12.66,0,0,1,4.17,1.3,7.94,7.94,0,0,1,3,2.72,8.34,8.34,0,0,1,1.15,4.65,7.48,7.48,0,0,1-1.67,5,9.13,9.13,0,0,1-4.43,2.82V17a10.28,10.28,0,0,1,3.18,1,8.51,8.51,0,0,1,2.45,1.85,7.79,7.79,0,0,1,1.57,2.62,9.16,9.16,0,0,1,.55,3.2,8.52,8.52,0,0,1-1.2,4.68,9.32,9.32,0,0,1-3.1,3A13.38,13.38,0,0,1,152.32,35a22.5,22.5,0,0,1-4.73.5h-14.5Zm7.8,14.15h5.65a7.65,7.65,0,0,0,1.78-.2,4.78,4.78,0,0,0,1.57-.65,3.43,3.43,0,0,0,1.13-1.2,3.63,3.63,0,0,0,.42-1.8A3.3,3.3,0,0,0,151,8.6a3.42,3.42,0,0,0-1.23-1.13A6.07,6.07,0,0,0,148,6.9a9.9,9.9,0,0,0-1.85-.18h-5.3Zm0,14.65h7a8.27,8.27,0,0,0,1.83-.2,4.67,4.67,0,0,0,1.67-.7,3.93,3.93,0,0,0,1.23-1.3,3.8,3.8,0,0,0,.47-1.95,3.16,3.16,0,0,0-.62-2,4,4,0,0,0-1.58-1.18,8.23,8.23,0,0,0-2-.55,15.12,15.12,0,0,0-2.05-.15h-5.9Z"""

@Composable
private fun MovieNavigationPreview(
    darkTheme: Boolean,
    currentRoute: String,
    showBack: Boolean = false
) {
    MovieLibraryProjectTheme(darkTheme = darkTheme, dynamicColor = false) {
        val isDetails = currentRoute == DetailsRoute
        val title = stringResource(
            when (currentRoute) {
                GenresRoute -> R.string.navigation_genres
                FavoritesRoute -> R.string.navigation_favorites
                DetailsRoute -> R.string.navigation_details
                else -> R.string.navigation_catalog
            }
        )
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                MovieTopBar(title = title, showBack = showBack, onBackClick = {}, onAboutClick = {})
            },
            bottomBar = {
                if (!isDetails) {
                    MovieBottomBar(currentRoute = currentRoute, onSectionClick = {})
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            )
        }
    }
}

@Preview(name = "Navigation light", showBackground = true, showSystemUi = true)
@Composable
private fun NavigationLightPreview() {
    MovieNavigationPreview(darkTheme = false, currentRoute = CatalogRoute)
}

@Preview(name = "Navigation dark", showBackground = true, showSystemUi = true)
@Composable
private fun NavigationDarkPreview() {
    MovieNavigationPreview(darkTheme = true, currentRoute = FavoritesRoute)
}

@Preview(name = "Genres light", showBackground = true, showSystemUi = true)
@Composable
private fun GenresNavigationPreview() {
    MovieNavigationPreview(darkTheme = false, currentRoute = GenresRoute)
}

@Preview(name = "Details light", showBackground = true, showSystemUi = true)
@Composable
private fun DetailsNavigationLightPreview() {
    MovieNavigationPreview(darkTheme = false, currentRoute = DetailsRoute, showBack = true)
}

@Preview(name = "Details dark", showBackground = true, showSystemUi = true)
@Composable
private fun DetailsNavigationDarkPreview() {
    MovieNavigationPreview(darkTheme = true, currentRoute = DetailsRoute, showBack = true)
}
