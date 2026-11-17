package com.example.movielibraryproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movielibraryproject.ui.screens.CatalogScreen
import com.example.movielibraryproject.viewmodel.MovieViewModel

private const val CatalogRoute = "catalog"

@Composable
fun MovieLibraryApp() {
    val movieViewModel: MovieViewModel = viewModel()
    val uiState by movieViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogRoute
    ) {
        composable(CatalogRoute) {
            CatalogScreen(
                uiState = uiState,
                onQueryChange = movieViewModel::onSearchQueryChange,
                onMovieClick = {},
                onFavoriteClick = movieViewModel::toggleFavorite,
                onClearGenre = { movieViewModel.onGenreSelected(null) }
            )
        }
    }
}
