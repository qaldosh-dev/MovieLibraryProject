package com.example.movielibraryproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movielibraryproject.ui.navigation.MovieLibraryApp
import com.example.movielibraryproject.ui.theme.MovieLibraryProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MovieLibraryProjectTheme {
                MovieLibraryApp()
            }
        }
    }
}