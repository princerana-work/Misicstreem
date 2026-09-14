# MusicStream - Quick Setup (Copy-Paste Guide)

## 📋 Files to Create & Where to Paste

### 1️⃣ DATA MODELS
**Location**: `app/src/main/java/com/example/musicstream/data/model/Track.kt`
```
Copy content from → Track.kt
```

**Location**: `app/src/main/java/com/example/musicstream/data/remote/ApiServices.kt`
```
Copy content from → ApiServices.kt
```

---

### 2️⃣ DATABASE
**Location**: `app/src/main/java/com/example/musicstream/data/local/MusicDatabase.kt`
```
Copy content from → MusicDatabase.kt
```

---

### 3️⃣ REPOSITORY
**Location**: `app/src/main/java/com/example/musicstream/data/repository/MusicRepository.kt`
```
Copy content from → MusicRepository.kt
```

---

### 4️⃣ DEPENDENCY INJECTION
**Location**: `app/src/main/java/com/example/musicstream/di/HiltModule.kt`
```
Copy content from → HiltModule.kt
```

---

### 5️⃣ VIEW MODELS
**Location**: `app/src/main/java/com/example/musicstream/presentation/viewmodel/ViewModels.kt`
```
Copy content from → ViewModels.kt
```

---

### 6️⃣ UI COMPONENTS
**Location**: `app/src/main/java/com/example/musicstream/ui/components/ComposeComponents.kt`
```
Copy content from → ComposeComponents.kt
```

---

### 7️⃣ THEME
**Location**: `app/src/main/java/com/example/musicstream/ui/theme/Theme.kt`
```
Paste this:

package com.example.musicstream.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1DB954),
    secondary = Color(0xFF1ed760),
    tertiary = Color(0xFFbb86fc),
    background = Color(0xFF121212),
    surface = Color(0xFF282828)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1DB954),
    secondary = Color(0xFF1ed760),
    tertiary = Color(0xFFbb86fc),
    background = Color.White,
    surface = Color(0xFFF5F5F5)
)

@Composable
fun MusicStreamTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(colorScheme = colorScheme, content = content)
}
```

---

### 8️⃣ MAIN ACTIVITY & SCREENS
**Location**: `app/src/main/java/com/example/musicstream/MainActivity.kt`
```
Copy content from → MainActivityAndScreens.kt
```

---

### 9️⃣ BUILD.GRADLE FILES

**Location**: `build.gradle.kts` (Project level)
```gradle
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

**Location**: `build.gradle.kts` (App level)
```gradle
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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.media3:media3-exoplayer:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
}
```

---

### 🔟 MANIFEST
**Location**: `app/src/main/AndroidManifest.xml`

Add inside `<manifest>`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
```

---

## ⚡ Installation Steps

### Step 1: Create Project
```
Android Studio → File → New → Android Project
- Name: MusicStream
- Package: com.example.musicstream
- Language: Kotlin
- Min SDK: 28
```

### Step 2: Create Folders
```
Right-click com.example.musicstream → New → Package
Create:
- data/model
- data/local
- data/remote
- data/repository
- di
- presentation/viewmodel
- ui/components
- ui/theme
```

### Step 3: Add Dependencies
- Replace `build.gradle` files with code above
- Click "Sync Now"

### Step 4: Create Files
- Use table above to create each file
- Copy-paste content from corresponding .kt file

### Step 5: Add Permissions
- Copy manifest permissions from above
- Paste into AndroidManifest.xml

### Step 6: Build
```
Build → Make Project
(Fix any errors)
```

### Step 7: Run
```
Run → Run 'app'
Select emulator or device
```

---

## 🔑 Add API Keys

### Spotify
1. Go to https://developer.spotify.com/dashboard
2. Create app → Get Client ID & Secret
3. Implement OAuth flow

### YouTube
1. Go to https://console.cloud.google.com
2. Enable YouTube Data API v3
3. Create credentials → Get API Key
4. Add to SharedPreferences in app

---

## ✅ Test Checklist

- [ ] App builds without errors
- [ ] App runs on emulator/device
- [ ] Home screen shows playlists
- [ ] Search screen works
- [ ] Click song to play
- [ ] Player bar shows at bottom
- [ ] Click player bar to see full screen
- [ ] Play/Pause buttons work

---

## 📁 Final Project Structure

```
app/src/main/
├── java/com/example/musicstream/
│   ├── data/
│   │   ├── local/MusicDatabase.kt
│   │   ├── model/Track.kt
│   │   ├── remote/ApiServices.kt
│   │   └── repository/MusicRepository.kt
│   ├── di/HiltModule.kt
│   ├── presentation/viewmodel/ViewModels.kt
│   ├── ui/
│   │   ├── components/ComposeComponents.kt
│   │   └── theme/Theme.kt
│   └── MainActivity.kt
└── AndroidManifest.xml
```

---

## 🚀 You're Ready!

Now you have a fully functional Android music streaming app!

Next steps:
1. Add more screens (artist details, album view, etc.)
2. Implement actual playback using ExoPlayer
3. Add offline mode
4. Deploy to GitHub
5. Publish on Play Store

Happy coding! 🎵
