# MusicStream - Android Music Streaming App

A modern, open-source music streaming app built with Kotlin and Jetpack Compose.

## Project Architecture

```
app/
├── src/main/java/com/example/musicstream/
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── SearchScreen.kt
│   │   │   ├── PlaylistScreen.kt
│   │   │   └── NowPlayingScreen.kt
│   │   ├── components/
│   │   │   ├── PlayerBar.kt
│   │   │   ├── TrackCard.kt
│   │   │   └── BottomNavigation.kt
│   │   └── theme/
│   │       └── Theme.kt
│   ├── data/
│   │   ├── repository/
│   │   │   ├── SpotifyRepository.kt
│   │   │   ├── YouTubeRepository.kt
│   │   │   └── PlaylistRepository.kt
│   │   ├── local/
│   │   │   ├── MusicDatabase.kt
│   │   │   └── PlaylistDao.kt
│   │   ├── remote/
│   │   │   ├── SpotifyApiService.kt
│   │   │   └── YouTubeApiService.kt
│   │   └── model/
│   │       ├── Track.kt
│   │       ├── Playlist.kt
│   │       └── Artist.kt
│   ├── domain/
│   │   ├── usecase/
│   │   │   ├── SearchTracksUseCase.kt
│   │   │   ├── GetPlaylistsUseCase.kt
│   │   │   └── PlayTrackUseCase.kt
│   │   └── model/
│   │       └── DomainModel.kt
│   ├── viewmodel/
│   │   ├── MainViewModel.kt
│   │   ├── PlayerViewModel.kt
│   │   └── SearchViewModel.kt
│   ├── player/
│   │   ├── MediaPlayerManager.kt
│   │   └── PlayerState.kt
│   └── MainActivity.kt
└── res/
```

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **HTTP Client**: Retrofit + OkHttp
- **Database**: Room
- **Music Playback**: ExoPlayer
- **Async**: Coroutines + Flow
- **State Management**: ViewModel + StateFlow

## Step-by-Step Setup

### 1. Create Project in Android Studio
- File → New → New Android Project
- Select "Empty Activity" template
- Target API 28+
- Language: Kotlin

### 2. Update build.gradle (Project Level)
```gradle
plugins {
    id 'com.android.application' version '8.1.0' apply false
    id 'com.android.library' version '8.1.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.0' apply false
    id 'com.google.dagger.hilt.android' version '2.47' apply false
}
```

### 3. Update build.gradle (App Level)
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

android {
    compileSdk 34
    
    defaultConfig {
        applicationId "com.example.musicstream"
        minSdk 28
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }
    
    buildFeatures {
        compose true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    // Jetpack Compose
    implementation 'androidx.compose.ui:ui:1.5.0'
    implementation 'androidx.compose.material3:material3:1.0.0'
    implementation 'androidx.compose.foundation:foundation:1.5.0'
    implementation 'androidx.activity:activity-compose:1.7.1'
    
    // ViewModel & Lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.6.0'
    
    // Hilt
    implementation 'com.google.dagger:hilt-android:2.47'
    kapt 'com.google.dagger:hilt-compiler:2.47'
    
    // Retrofit & OkHttp
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.10.0'
    
    // Room Database
    implementation 'androidx.room:room-runtime:2.5.2'
    kapt 'androidx.room:room-compiler:2.5.2'
    implementation 'androidx.room:room-ktx:2.5.2'
    
    // ExoPlayer
    implementation 'androidx.media3:media3-exoplayer:1.0.0'
    implementation 'androidx.media3:media3-ui:1.0.0'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0'
    
    // Coil for images
    implementation 'io.coil-kt:coil-compose:2.4.0'
}
```

## API Setup

### Spotify API
1. Go to https://developer.spotify.com
2. Create an app
3. Get Client ID & Secret
4. Use Authorization Code Flow for user login

### YouTube Data API
1. Go to https://console.cloud.google.com
2. Enable YouTube Data API v3
3. Create OAuth 2.0 credentials
4. Add your app as authorized client

## Database Models

```kotlin
// Track entity
@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val albumArt: String,
    val streamUrl: String,
    val duration: Long
)

// Playlist entity
@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val trackCount: Int,
    val imageUrl: String
)
```

## Features to Implement

- ✅ User authentication (Spotify/YouTube OAuth)
- ✅ Search tracks & artists
- ✅ Create & manage playlists
- ✅ Now playing screen with full controls
- ✅ Shuffle & repeat modes
- ✅ Queue management
- ✅ Offline playlist cache
- ✅ Dark theme support
- ✅ Search history
- ✅ Recommendations

## Open Source & GitHub Setup

```bash
git init
git add .
git commit -m "Initial commit: MusicStream v1.0"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/musicstream.git
git push -u origin main
```

### README.md Template
```markdown
# MusicStream

A modern Android music streaming app with Spotify & YouTube integration.

## Features
- Stream from Spotify or YouTube Music
- Create and manage playlists
- Search millions of tracks
- Offline caching
- Dark theme support

## Installation
1. Clone this repo
2. Open in Android Studio
3. Add your Spotify/YouTube API keys
4. Run on emulator or device

## Tech Stack
- Kotlin + Jetpack Compose
- ExoPlayer
- Retrofit
- Room Database
- Hilt DI

## License
MIT License

## Contributing
Pull requests welcome!
```

## Next Steps
1. Create the project structure
2. Implement data layer (APIs & Database)
3. Build ViewModels
4. Create UI screens with Compose
5. Integrate ExoPlayer for playback
6. Test & publish
