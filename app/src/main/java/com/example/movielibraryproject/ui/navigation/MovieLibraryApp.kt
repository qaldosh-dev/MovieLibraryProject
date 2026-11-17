package com.example.movielibraryproject.ui.navigation

import android.net.Uri
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movielibraryproject.ui.screens.CatalogScreen
import com.example.movielibraryproject.ui.screens.GenresScreen
import com.example.movielibraryproject.ui.screens.FavoritesScreen
import com.example.movielibraryproject.viewmodel.MovieViewModel

private const val CatalogRoute = "catalog?genre={genre}"
private const val GenresRoute = "genres"
private const val FavoritesRoute = "favorites"

@Composable
fun MovieLibraryApp() {
    val movieViewModel: MovieViewModel = viewModel()
    val uiState by movieViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogRoute
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
                onMovieClick = {},
                onFavoriteClick = movieViewModel::toggleFavorite,
                onClearGenre = {
                    navController.navigate("catalog") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
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
                onMovieClick = {},
                onFavoriteClick = movieViewModel::toggleFavorite
            )
        }
    }
}
