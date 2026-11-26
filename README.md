# Movie Library

Movie Library is an Android app for discovering movies and keeping a personal collection. We built it with Kotlin and Jetpack Compose for our Mobile Development course. The catalog uses live data from The Movie Database (TMDB), and saved movies and notes stay on the device.

## Features

- Browse movies from TMDB, search the loaded catalog by title, and filter it by genre.
- View movie details, including the poster, release year, genres, rating, and overview. Coil loads posters from TMDB image URLs.
- See loading, empty, and error states. Use **Retry** if the catalog fails to load.
- Add or remove favorites and edit a personal note for each saved movie. Favorites and notes are available offline after reopening the app.
- Sort favorites by title or by most recently added. The app remembers the selected order after a restart.

## Navigation

Navigation Compose connects Catalog, Genres, Favorites, and Movie Details. Selecting a genre opens a filtered catalog. Selecting a film passes its ID and the source screen to Details, so a saved film can be opened from Favorites even when the online catalog is unavailable.

## MVVM and Data

- **Network:** Retrofit fetches the TMDB catalog. Gson reads the JSON into Kotlin DTOs, which the remote repository maps to the app's movie model.
- **Local storage:** Room stores favorite movies and notes in the `favorite_movies` table. Preferences DataStore stores the selected sort order.
- **UI state:** `MovieViewModel` uses Kotlin coroutines to load data and exposes state through `StateFlow`. Compose observes that state and sends user actions back to the ViewModel.

## GitHub Workflow

We tracked tasks in GitHub Issues, worked on separate feature branches, and opened pull requests for review. GitHub Actions builds the app and runs Android Lint and unit tests.

## Team

- **Bauyrzhan** — team lead, catalog, API loading and retry, Room integration in the ViewModel, and applying the saved sort order.
- **Aigerim** — movie details, app icon, Room database and DAO, note editing, and persistence tests.
- **Ayaulym** — theme, movie cards, Favorites screen, Coil posters, and DataStore sorting preferences and controls.
- **Niyazbek** — genres, navigation, TMDB integration, and the About dialog with TMDB attribution.
