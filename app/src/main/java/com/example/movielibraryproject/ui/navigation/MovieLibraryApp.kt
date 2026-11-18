package com.example.movielibraryproject.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.movielibraryproject.viewmodel.MovieViewModel

private const val CatalogRoute = "catalog?genre={genre}"
private const val GenresRoute = "genres"
private const val FavoritesRoute = "favorites"
private const val DetailsRoute = "details/{movieId}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieLibraryApp() {
    val movieViewModel: MovieViewModel = viewModel()
    val uiState by movieViewModel.uiState.collectAsState()
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
    val bottomDestinations = listOf(
        "catalog" to R.string.navigation_catalog,
        GenresRoute to R.string.navigation_genres,
        FavoritesRoute to R.string.navigation_favorites
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (isDetails || isFilteredCatalog) {
                        TextButton(onClick = { navController.popBackStack() }) {
                            Text(stringResource(R.string.navigation_back))
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (!isDetails) {
                NavigationBar {
                    bottomDestinations.forEach { (route, labelRes) ->
                        val label = stringResource(labelRes)
                        val selected = if (route == "catalog") {
                            currentRoute == CatalogRoute
                        } else {
                            currentRoute == route
                        }
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navController.openMainSection(route) },
                            icon = { Text(label.take(1)) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CatalogRoute,
            modifier = Modifier.padding(innerPadding)
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
