package com.example.musicstream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musicstream.data.repository.Result
import com.example.musicstream.presentation.viewmodel.HomeViewModel
import com.example.musicstream.presentation.viewmodel.PlayerViewModel
import com.example.musicstream.presentation.viewmodel.SearchViewModel
import com.example.musicstream.ui.components.*
import com.example.musicstream.ui.theme.MusicStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicStreamTheme {
                MusicStreamApp()
            }
        }
    }
}

@Composable
fun MusicStreamApp() {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    
    val currentTrack by playerViewModel.currentTrack.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    
    Scaffold(
        bottomBar = {
            Column {
                if (currentTrack != null) {
                    PlayerBar(
                        track = currentTrack,
                        isPlaying = isPlaying,
                        onPlayPauseClick = { playerViewModel.togglePlayPause() },
                        onPlayerBarClick = { navController.navigate("now_playing") }
                    )
                }
                MusicAppBottomNavigation(
                    selectedTab = navController.currentBackStackEntry?.destination?.route ?: "home",
                    onTabSelected = { tab ->
                        navController.navigate(tab) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("search") {
                SearchScreen(navController)
            }
            composable("playlists") {
                PlaylistsScreen(navController)
            }
            composable("favorites") {
                FavoritesScreen(navController)
            }
            composable("now_playing") {
                NowPlayingScreen(navController)
            }
        }
    }
}

// ============ HOME SCREEN ============
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userPlaylists by viewModel.userPlaylists.collectAsState()
    val featuredPlaylists by viewModel.featuredPlaylists.collectAsState()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Featured Playlists
        Text(
            text = "Featured",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        
        when (val result = featuredPlaylists) {
            is Result.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Result.Success -> {
                PlaylistGrid(
                    playlists = result.data,
                    onPlaylistClick = { playlist ->
                        navController.navigate("playlist/${playlist.id}")
                    }
                )
            }
            is Result.Error -> {
                Text("Error loading playlists")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Your Playlists
        Text(
            text = "Your Playlists",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        
        when (val result = userPlaylists) {
            is Result.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is Result.Success -> {
                PlaylistGrid(
                    playlists = result.data,
                    onPlaylistClick = { playlist ->
                        navController.navigate("playlist/${playlist.id}")
                    }
                )
            }
            is Result.Error -> {
                Text("Error loading your playlists")
            }
        }
    }
}

// ============ SEARCH SCREEN ============
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSource by viewModel.selectedSource.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val playerViewModel: PlayerViewModel = hiltViewModel()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = { viewModel.performSearch(it) },
            modifier = Modifier.padding(16.dp)
        )
        
        // Source Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedSource == "spotify",
                onClick = { viewModel.selectSource("spotify") },
                label = { Text("Spotify") }
            )
            FilterChip(
                selected = selectedSource == "youtube",
                onClick = { viewModel.selectSource("youtube") },
                label = { Text("YouTube") }
            )
        }
        
        // Results
        when (val result = searchResults) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp)
                )
            }
            is Result.Success -> {
                TrackList(
                    tracks = result.data,
                    onTrackClick = { track ->
                        playerViewModel.setQueue(listOf(track), 0)
                        playerViewModel.play()
                    }
                )
            }
            is Result.Error -> {
                Text(
                    "Error: ${result.exception.message}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// ============ PLAYLISTS SCREEN ============
@Composable
fun PlaylistsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Your Playlists")
    }
}

// ============ FAVORITES SCREEN ============
@Composable
fun FavoritesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Your Favorites")
    }
}

// ============ NOW PLAYING SCREEN ============
@Composable
fun NowPlayingScreen(
    navController: NavController,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val repeatMode by viewModel.repeatMode.collectAsState()
    val shuffle by viewModel.shuffle.collectAsState()
    
    if (currentTrack == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No track playing")
        }
        return
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Album Art
        AsyncImage(
            model = currentTrack?.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        
        // Track Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = currentTrack?.title ?: "",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = currentTrack?.artist ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Progress Bar
        PlayerProgressBar(
            currentPosition = currentPosition,
            duration = duration,
            onSeek = { viewModel.seekTo(it) }
        )
        
        // Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.toggleShuffle() }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Shuffle",
                    tint = if (shuffle) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.onSurface
                )
            }
            
            IconButton(onClick = { viewModel.previousTrack() }) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
            }
            
            IconButton(
                onClick = { viewModel.togglePlayPause() },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            IconButton(onClick = { viewModel.nextTrack() }) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next")
            }
            
            IconButton(onClick = { viewModel.cycleRepeatMode() }) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Repeat"
                )
            }
        }
    }
}

// Add missing imports
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext as SkipNextIcon
import androidx.compose.material.icons.filled.SkipPrevious as SkipPreviousIcon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.rememberScrollState
import coil.compose.AsyncImage
