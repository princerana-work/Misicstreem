# MusicStream - Complete Implementation Guide

## Step 1: Create Project in Android Studio

1. **Open Android Studio**
2. **File → New → New Android Project**
3. **Select Template**: "Empty Activity"
4. **Configure Project**:
   - Name: `MusicStream`
   - Package: `com.example.musicstream`
   - Save Location: Choose folder
   - Language: Kotlin
   - Minimum API: 28
   - Build Configuration: Kotlin DSL (or Groovy)

5. Click **Finish** and wait for project to build

---

## Step 2: Update build.gradle Files

### 2.1 Project-level build.gradle.kts

Replace the entire content of `build.gradle.kts` (Project: MusicStream):

```kotlin
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

task("clean") {
    delete(rootProject.buildDir)
}
```

### 2.2 App-level build.gradle.kts

Replace the entire content of `build.gradle.kts` (Module: app):

```kotlin
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicstream"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")

    // Retrofit & OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // ExoPlayer (Media Player)
    implementation("androidx.media3:media3-exoplayer:1.0.0")
    implementation("androidx.media3:media3-ui:1.0.0")
    implementation("androidx.media3:media3-session:1.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

    // Coil (Image Loading)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

Click **Sync Now** when prompted.

---

## Step 3: Create Package Structure

In Android Studio:
1. Right-click on `com.example.musicstream` in Project view
2. **New → Package**
3. Create these packages:

```
com.example.musicstream
├── data
│   ├── local
│   ├── model
│   ├── remote
│   └── repository
├── di
├── player
├── presentation
│   └── viewmodel
└── ui
    ├── components
    ├── screens
    └── theme
```

---

## Step 4: Create Data Models

### 4.1 Create `Track.kt`

1. **Right-click** `com.example.musicstream.data.model` → **New → Kotlin Class/File**
2. Copy content from **Track.kt** (from files we created)
3. Paste into new file

### 4.2 Repeat for:
- `ApiServices.kt` (in `data.remote`)
- `MusicDatabase.kt` (in `data.local`)

---

## Step 5: Create Repository

1. **Right-click** `com.example.musicstream.data.repository` → **New → Kotlin Class/File**
2. Name: `MusicRepository.kt`
3. Copy content from **MusicRepository.kt** we created
4. Paste into file

---

## Step 6: Create Dependency Injection Module

1. **Right-click** `com.example.musicstream.di` → **New → Kotlin Class/File**
2. Name: `HiltModule.kt`
3. Copy content from **HiltModule.kt**
4. Paste into file

---

## Step 7: Create ViewModels

1. **Right-click** `com.example.musicstream.presentation.viewmodel` → **New → Kotlin Class/File**
2. Name: `ViewModels.kt`
3. Copy content from **ViewModels.kt**
4. Paste into file

---

## Step 8: Create UI Components

1. **Right-click** `com.example.musicstream.ui.components` → **New → Kotlin Class/File**
2. Name: `ComposeComponents.kt`
3. Copy content from **ComposeComponents.kt**
4. Paste into file

---

## Step 9: Create Theme

1. **Right-click** `com.example.musicstream.ui.theme` → **New → Kotlin Class/File**
2. Name: `Theme.kt`
3. Paste this content:

```kotlin
package com.example.musicstream.ui.theme

import androidx.compose.foundation.dark
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1DB954),
    secondary = Color(0xFF1ed760),
    tertiary = Color(0xFFbb86fc),
    background = Color(0xFF121212),
    surface = Color(0xFF282828),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1DB954),
    secondary = Color(0xFF1ed760),
    tertiary = Color(0xFFbb86fc),
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun MusicStreamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
```

---

## Step 10: Create Main Activity

1. Replace **MainActivity.kt** with content from **MainActivityAndScreens.kt**
2. Make sure package name is correct: `package com.example.musicstream`

---

## Step 11: Update AndroidManifest.xml

Add these permissions to `AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MusicStream"
        tools:targetApi="31">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.MusicStream">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

---

## Step 12: Set Up API Keys

### 12.1 Spotify API

1. Go to https://developer.spotify.com/dashboard
2. Create an account
3. Create an app and get:
   - **Client ID**
   - **Client Secret**

4. In your app, save token via:
```kotlin
val preferences = context.getSharedPreferences("musicstream_prefs", Context.MODE_PRIVATE)
preferences.edit().putString("spotify_token", "your_token_here").apply()
```

### 12.2 YouTube API

1. Go to https://console.cloud.google.com
2. Create a new project
3. Enable **YouTube Data API v3**
4. Create **OAuth 2.0 credentials**
5. Get your **API Key**

6. Save in app:
```kotlin
preferences.edit().putString("youtube_api_key", "your_api_key_here").apply()
```

---

## Step 13: Build & Run

1. **Build → Make Project** (or Ctrl+F9)
2. Fix any errors
3. **Run → Run 'app'** (or Shift+F10)
4. Select emulator or device
5. Wait for installation to complete

---

## Step 14: Test the App

✅ Click **Home** → See featured playlists  
✅ Click **Search** → Search for songs  
✅ Click **Search** → Pick a song → It should play  
✅ Click **Player Bar** → See full now playing screen  

---

## Troubleshooting

### "Can't resolve symbol 'MusicStreamTheme'"
- Make sure `Theme.kt` is in correct package
- Rebuild project

### "Retrofit call failed"
- Add your API tokens first
- Check internet connection
- Verify API credentials

### "Cannot find symbol 'HiltViewModel'"
- Check Hilt dependency version
- Run `Build → Clean Project`

### Compose preview not showing
- Click **Build → Make Project**
- Click **Split** view mode
- Wait for compilation

---

## Next: Add ExoPlayer Integration

Create `MediaPlayerManager.kt`:

```kotlin
package com.example.musicstream.player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.Player

class MediaPlayerManager(context: Context) {
    val player = ExoPlayer.Builder(context).build()
    
    fun playTrack(url: String, title: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    
    fun pause() = player.pause()
    
    fun resume() = player.play()
    
    fun seekTo(position: Long) = player.seekTo(position)
    
    fun release() = player.release()
}
```

---

## Final Checklist

- [ ] Project created
- [ ] build.gradle updated
- [ ] All packages created
- [ ] All Kotlin files created
- [ ] Permissions added to manifest
- [ ] API keys configured
- [ ] Project builds without errors
- [ ] App runs on emulator/device
- [ ] Search works
- [ ] Player bar shows

**Congrats! Your music streaming app is ready! 🎵**
