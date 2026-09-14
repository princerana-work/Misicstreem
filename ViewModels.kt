package com.example.musicstream.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicstream.data.model.Playlist
import com.example.musicstream.data.model.Track
import com.example.musicstream.data.model.TrackUI
import com.example.musicstream.data.repository.MusicRepository
import com.example.musicstream.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ============ SEARCH VIEW MODEL ============
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    
    private val _selectedSource = MutableStateFlow("spotify")
    val selectedSource = _selectedSource.asStateFlow()
    
    private val _searchResults = MutableStateFlow<Result<List<TrackUI>>>(Result.Loading)
    val searchResults = _searchResults.asStateFlow()
    
    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun selectSource(source: String) {
        _selectedSource.value = source
    }
    
    fun performSearch(query: String) {
        if (query.isBlank()) {
            _searchResults.value = Result.Success(emptyList())
            return
        }
        
        _searchQuery.value = query
        addToSearchHistory(query)
        
        viewModelScope.launch {
            repository.searchTracks(query, _selectedSource.value)
                .collect { result ->
                    _searchResults.value = result
                }
        }
    }
    
    private fun addToSearchHistory(query: String) {
        val current = _searchHistory.value.toMutableList()
        if (current.contains(query)) {
            current.remove(query)
        }
        current.add(0, query)
        if (current.size > 20) {
            current.removeAt(current.size - 1)
        }
        _searchHistory.value = current
    }
    
    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }
}

// ============ PLAYER VIEW MODEL ============
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _currentTrack = MutableStateFlow<TrackUI?>(null)
    val currentTrack = _currentTrack.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0L)
    val duration = _duration.asStateFlow()
    
    private val _queue = MutableStateFlow<List<TrackUI>>(emptyList())
    val queue = _queue.asStateFlow()
    
    private val _currentQueueIndex = MutableStateFlow(0)
    val currentQueueIndex = _currentQueueIndex.asStateFlow()
    
    private val _shuffle = MutableStateFlow(false)
    val shuffle = _shuffle.asStateFlow()
    
    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)
    val repeatMode = _repeatMode.asStateFlow()
    
    fun setQueue(tracks: List<TrackUI>, startIndex: Int = 0) {
        _queue.value = tracks
        _currentQueueIndex.value = startIndex
        if (startIndex < tracks.size) {
            _currentTrack.value = tracks[startIndex]
        }
    }
    
    fun play() {
        _isPlaying.value = true
    }
    
    fun pause() {
        _isPlaying.value = false
    }
    
    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }
    
    fun nextTrack() {
        val current = _currentQueueIndex.value
        val queue = _queue.value
        
        if (queue.isEmpty()) return
        
        val nextIndex = when {
            _shuffle.value -> (0 until queue.size).random()
            current < queue.size - 1 -> current + 1
            _repeatMode.value == RepeatMode.ALL -> 0
            else -> current
        }
        
        _currentQueueIndex.value = nextIndex
        _currentTrack.value = queue.getOrNull(nextIndex)
        _currentPosition.value = 0L
    }
    
    fun previousTrack() {
        val current = _currentQueueIndex.value
        val prevIndex = if (current > 0) current - 1 else 0
        
        _currentQueueIndex.value = prevIndex
        _currentTrack.value = _queue.value.getOrNull(prevIndex)
        _currentPosition.value = 0L
    }
    
    fun seekTo(position: Long) {
        _currentPosition.value = position
    }
    
    fun updateCurrentPosition(position: Long) {
        _currentPosition.value = position
    }
    
    fun setDuration(duration: Long) {
        _duration.value = duration
    }
    
    fun toggleShuffle() {
        _shuffle.value = !_shuffle.value
    }
    
    fun cycleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.NONE -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.NONE
        }
    }
    
    fun removeFromQueue(index: Int) {
        val queue = _queue.value.toMutableList()
        queue.removeAt(index)
        _queue.value = queue
    }
    
    fun saveCurrentTrackToFavorites() {
        val track = _currentTrack.value ?: return
        viewModelScope.launch {
            repository.saveTrack(
                Track(
                    id = track.id,
                    title = track.title,
                    artist = track.artist,
                    album = track.album,
                    albumArtUrl = track.imageUrl,
                    streamUrl = null,
                    duration = track.duration,
                    source = "manual"
                )
            )
        }
    }
}

enum class RepeatMode {
    NONE, ALL, ONE
}

// ============ HOME VIEW MODEL ============
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _userPlaylists = MutableStateFlow<Result<List<Playlist>>>(Result.Loading)
    val userPlaylists = _userPlaylists.asStateFlow()
    
    private val _featuredPlaylists = MutableStateFlow<Result<List<Playlist>>>(Result.Loading)
    val featuredPlaylists = _featuredPlaylists.asStateFlow()
    
    private val _favorites = MutableStateFlow<List<Track>>(emptyList())
    val favorites = _favorites.asStateFlow()
    
    init {
        loadUserPlaylists()
        loadFeaturedPlaylists()
        loadFavorites()
    }
    
    private fun loadUserPlaylists() {
        viewModelScope.launch {
            repository.getUserPlaylists().collect { result ->
                _userPlaylists.value = result
            }
        }
    }
    
    private fun loadFeaturedPlaylists() {
        viewModelScope.launch {
            repository.getFeaturedPlaylists().collect { result ->
                _featuredPlaylists.value = result
            }
        }
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteTracks().collect { tracks ->
                _favorites.value = tracks
            }
        }
    }
}

// ============ PLAYLIST VIEW MODEL ============
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: MusicRepository
) : ViewModel() {
    
    private val _playlistId = MutableStateFlow("")
    
    private val _playlistDetails = MutableStateFlow<Playlist?>(null)
    val playlistDetails = _playlistDetails.asStateFlow()
    
    private val _playlistTracks = MutableStateFlow<List<Track>>(emptyList())
    val playlistTracks = _playlistTracks.asStateFlow()
    
    fun setPlaylistId(id: String) {
        _playlistId.value = id
        loadPlaylistTracks(id)
    }
    
    private fun loadPlaylistTracks(playlistId: String) {
        viewModelScope.launch {
            repository.getPlaylistTracks(playlistId).collect { tracks ->
                _playlistTracks.value = tracks
            }
        }
    }
    
    fun addTrackToPlaylist(track: Track) {
        viewModelScope.launch {
            repository.addTrackToPlaylist(_playlistId.value, track)
        }
    }
    
    fun createNewPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            val playlist = Playlist(
                id = System.currentTimeMillis().toString(),
                name = name,
                description = description,
                imageUrl = "",
                trackCount = 0,
                isPublic = false,
                owner = "User"
            )
            repository.createLocalPlaylist(playlist)
        }
    }
    
    fun deletePlaylist() {
        viewModelScope.launch {
            repository.deletePlaylist(_playlistId.value)
        }
    }
}
