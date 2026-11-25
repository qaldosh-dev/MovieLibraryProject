package com.example.movielibraryproject.ui.navigation

import android.net.Uri
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
private const val DetailsRoute = "details/{movieId}"

@Composable
fun MovieLibraryApp() {
    val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
    val uiState by movieViewModel.uiState.collectAsState()

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
                onBackClick = { navController.popBackStack() }
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
                        navController.navigate("details/$movieId") {
                            launchSingleTop = true
                        }
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
                        navController.navigate("details/$movieId") {
                            launchSingleTop = true
                        }
                    },
                    onFavoriteClick = movieViewModel::toggleFavorite
                )
            }
            composable(
                route = DetailsRoute,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) { entry ->
                val movieId = entry.arguments?.getInt("movieId")
                val movie = uiState.movies.find { it.id == movieId }
                DetailsScreen(
                    movie = movie,
                    isFavorite = movie != null && movie.id in uiState.favoriteIds,
                    onFavoriteClick = {
                        movie?.let { movieViewModel.toggleFavorite(it.id) }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieTopBar(
    title: String,
    showBack: Boolean,
    onBackClick: () -> Unit
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
                MovieTopBar(title = title, showBack = showBack, onBackClick = {})
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
