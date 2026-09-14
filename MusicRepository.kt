package com.example.musicstream.data.repository

import android.content.SharedPreferences
import com.example.musicstream.data.local.MusicDatabase
import com.example.musicstream.data.model.Playlist
import com.example.musicstream.data.model.Track
import com.example.musicstream.data.model.TrackUI
import com.example.musicstream.data.remote.SpotifyApiService
import com.example.musicstream.data.remote.YouTubeApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class MusicRepository @Inject constructor(
    private val spotifyApi: SpotifyApiService,
    private val youtubeApi: YouTubeApiService,
    private val database: MusicDatabase,
    private val prefs: SharedPreferences
) {
    
    private val spotifyToken: String
        get() = prefs.getString("spotify_token", "") ?: ""
    
    private val youtubeApiKey: String
        get() = prefs.getString("youtube_api_key", "") ?: ""
    
    // ============ SEARCH ============
    fun searchTracks(query: String, source: String = "spotify"): Flow<Result<List<TrackUI>>> = flow {
        emit(Result.Loading)
        try {
            val result = when (source) {
                "spotify" -> searchSpotify(query)
                "youtube" -> searchYouTube(query)
                else -> emptyList()
            }
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    private suspend fun searchSpotify(query: String): List<TrackUI> {
        return try {
            val response = spotifyApi.searchTracks(
                query = query,
                token = "Bearer $spotifyToken"
            )
            response.items.map { track ->
                val artist = track.artists.firstOrNull()?.name ?: "Unknown"
                val imageUrl = track.album.images.firstOrNull()?.url ?: ""
                
                TrackUI(
                    id = track.id,
                    title = track.name,
                    artist = artist,
                    album = track.album.name,
                    imageUrl = imageUrl,
                    duration = track.duration
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    private suspend fun searchYouTube(query: String): List<TrackUI> {
        return try {
            val response = youtubeApi.searchVideos(
                query = query,
                apiKey = youtubeApiKey
            )
            response.items.map { video ->
                val imageUrl = video.snippet.thumbnails["high"]?.url ?: ""
                
                TrackUI(
                    id = video.id.videoId,
                    title = video.snippet.title,
                    artist = video.snippet.channelTitle,
                    album = "YouTube",
                    imageUrl = imageUrl,
                    duration = 0L // YouTube API doesn't return duration in search
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // ============ PLAYLISTS ============
    fun getUserPlaylists(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Loading)
        try {
            val response = spotifyApi.getUserPlaylists(
                token = "Bearer $spotifyToken"
            )
            val playlists = response.items.map { item ->
                val imageUrl = item.images?.firstOrNull()?.get("url")?.toString() ?: ""
                
                Playlist(
                    id = item.id,
                    name = item.name,
                    description = item.description ?: "",
                    imageUrl = imageUrl,
                    trackCount = item.tracks.total,
                    isPublic = true,
                    owner = item.owner.display_name
                )
            }
            // Cache in database
            database.playlistDao().insertPlaylists(playlists)
            emit(Result.Success(playlists))
        } catch (e: Exception) {
            // Fallback to cached data
            val cached = database.playlistDao().getAllPlaylists()
            emit(Result.Success(cached))
        }
    }
    
    fun getFeaturedPlaylists(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Loading)
        try {
            val response = spotifyApi.getFeaturedPlaylists(
                token = "Bearer $spotifyToken"
            )
            val playlists = response.playlists.items.map { item ->
                val imageUrl = item.images?.firstOrNull()?.get("url")?.toString() ?: ""
                
                Playlist(
                    id = item.id,
                    name = item.name,
                    description = item.description ?: "",
                    imageUrl = imageUrl,
                    trackCount = item.tracks.total,
                    isPublic = true,
                    owner = item.owner.display_name
                )
            }
            emit(Result.Success(playlists))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    // ============ LOCAL DATABASE ============
    fun getLocalPlaylists(): Flow<List<Playlist>> {
        return database.playlistDao().getAllPlaylistsFlow()
    }
    
    suspend fun createLocalPlaylist(playlist: Playlist) {
        database.playlistDao().insertPlaylist(playlist)
    }
    
    suspend fun deletePlaylist(playlistId: String) {
        database.playlistDao().deletePlaylist(playlistId)
    }
    
    suspend fun addTrackToPlaylist(playlistId: String, track: Track) {
        // Insert track if not exists
        database.trackDao().insertTrack(track)
        // Add to playlist
        database.playlistDao().addTrackToPlaylist(playlistId, track.id)
    }
    
    fun getPlaylistTracks(playlistId: String): Flow<List<Track>> {
        return database.playlistDao().getPlaylistTracks(playlistId)
    }
    
    // ============ FAVORITES ============
    suspend fun saveTrack(track: Track) {
        database.trackDao().insertTrack(track)
    }
    
    fun getFavoriteTracks(): Flow<List<Track>> {
        return database.trackDao().getAllTracksFlow()
    }
    
    // ============ AUTH ============
    fun setSpotifyToken(token: String) {
        prefs.edit().putString("spotify_token", token).apply()
    }
    
    fun setYouTubeApiKey(apiKey: String) {
        prefs.edit().putString("youtube_api_key", apiKey).apply()
    }
    
    fun hasSpotifyAuth(): Boolean {
        return spotifyToken.isNotEmpty()
    }
    
    fun hasYouTubeAuth(): Boolean {
        return youtubeApiKey.isNotEmpty()
    }
}
