# Movie Library

Movie Library is a small Android app for discovering movies and keeping a list of favorites during a session. The catalog includes six movies across Science Fiction, Drama, and Comedy, so you can browse by genre or look up a title you already know. We built it with Kotlin and Jetpack Compose for our Mobile Development course.

## Features

- **Browse the catalog:** See movie posters and a featured pick on the home screen.
- **Search by title:** Results update as you type, and the Clear button resets your search.
- **Explore genres:** Pick a genre to see the movies that belong to it.
- **View movie details:** Open a movie to see its poster, release year, genre, rating, and overview.
- **Manage favorites:** Tap the heart to save a movie or remove it from your collection.
- **Use the app offline:** Movies and posters are bundled with the app, and the interface follows the device's light or dark theme.

Favorites reset when the app process restarts.

## Navigation

The app uses Navigation Compose to connect Catalog, Genres, Favorites, and Movie Details. Selecting a genre opens a filtered catalog. Selecting a movie passes its ID to the Details screen.

## MVVM

The model describes a movie, and the repository provides the local catalog. A shared ViewModel uses StateFlow to manage search, genre filters, and favorites. The Compose screens display that state and send user actions back to the ViewModel.

## GitHub Workflow

We tracked tasks in GitHub Issues and worked on separate branches. We opened pull requests for review and used GitHub Actions to build the app, run unit tests, and check Android Lint before merging into main.

## Team

- Bauyrzhan — team lead, catalog, and shared state
- Aigerim — movie details, posters, and app icon
- Ayaulym — theme, movie cards, and Favorites
- Niyazbek — movie repository, genres, and navigation
