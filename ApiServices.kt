package com.example.musicstream.data.remote

import com.example.musicstream.data.model.SpotifyTrackResponse
import com.example.musicstream.data.model.YouTubeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// ============ SPOTIFY API ============
interface SpotifyApiService {
    
    @GET("v1/search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Header("Authorization") token: String
    ): SpotifyTrackResponse
    
    @GET("v1/me/playlists")
    suspend fun getUserPlaylists(
        @Query("limit") limit: Int = 50,
        @Header("Authorization") token: String
    ): SpotifyPlaylistResponse
    
    @GET("v1/browse/featured-playlists")
    suspend fun getFeaturedPlaylists(
        @Query("limit") limit: Int = 20,
        @Header("Authorization") token: String
    ): SpotifyFeaturedPlaylistResponse
    
    @GET("v1/recommendations")
    suspend fun getRecommendations(
        @Query("seed_artists") seedArtists: String,
        @Query("limit") limit: Int = 20,
        @Header("Authorization") token: String
    ): SpotifyTrackResponse
}

data class SpotifyPlaylistResponse(
    val items: List<SpotifyPlaylistItem>
)

data class SpotifyPlaylistItem(
    val id: String,
    val name: String,
    val description: String?,
    val images: List<Map<String, Any>>?,
    val tracks: TrackInfo,
    val owner: OwnerInfo
)

data class TrackInfo(
    val total: Int
)

data class OwnerInfo(
    val display_name: String
)

data class SpotifyFeaturedPlaylistResponse(
    val playlists: SpotifyPlaylistResponse
)

// ============ YOUTUBE API ============
interface YouTubeApiService {
    
    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("key") apiKey: String
    ): YouTubeSearchResponse
    
    @GET("youtube/v3/playlistItems")
    suspend fun getPlaylistItems(
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50,
        @Query("key") apiKey: String
    ): YouTubePlaylistItemResponse
}

data class YouTubePlaylistItemResponse(
    val items: List<YouTubePlaylistItem>
)

data class YouTubePlaylistItem(
    val id: String,
    val snippet: YouTubePlaylistItemSnippet
)

data class YouTubePlaylistItemSnippet(
    val title: String,
    val description: String,
    val thumbnails: Map<String, YouTubeThumbnailInfo>,
    val channelTitle: String,
    val videoId: String
)

data class YouTubeThumbnailInfo(
    val url: String,
    val width: Int,
    val height: Int
)
