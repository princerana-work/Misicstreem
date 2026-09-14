package com.example.musicstream.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Spotify API Track Response
data class SpotifyTrackResponse(
    @SerializedName("items")
    val items: List<SpotifyTrack>
)

data class SpotifyTrack(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("artists")
    val artists: List<SpotifyArtist>,
    @SerializedName("album")
    val album: SpotifyAlbum,
    @SerializedName("duration_ms")
    val duration: Long,
    @SerializedName("external_urls")
    val externalUrls: Map<String, String>,
    @SerializedName("preview_url")
    val previewUrl: String?
)

data class SpotifyArtist(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("external_urls")
    val externalUrls: Map<String, String>
)

data class SpotifyAlbum(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: List<SpotifyImage>
)

data class SpotifyImage(
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int
)

// Local Room Database Entities
@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val albumArtUrl: String,
    val streamUrl: String?,
    val duration: Long,
    val source: String, // "spotify", "youtube", "local"
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "artists")
data class Artist(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String,
    val genres: String, // comma-separated
    val popularity: Int
)

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val trackCount: Int,
    val isPublic: Boolean,
    val owner: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "playlist_tracks")
data class PlaylistTrack(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playlistId: String,
    val trackId: String,
    val addedAt: Long = System.currentTimeMillis()
)

// YouTube API Response
data class YouTubeSearchResponse(
    val items: List<YouTubeVideo>
)

data class YouTubeVideo(
    val id: YouTubeVideoId,
    val snippet: YouTubeSnippet
)

data class YouTubeVideoId(
    val videoId: String
)

data class YouTubeSnippet(
    val title: String,
    val description: String,
    val thumbnails: YouTubeThumbnails,
    val channelTitle: String
)

data class YouTubeThumbnails(
    val high: YouTubeThumbnail?
)

data class YouTubeThumbnail(
    val url: String,
    val height: Int,
    val width: Int
)

// UI Model (Unified)
data class TrackUI(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val imageUrl: String,
    val duration: Long,
    val isPlayable: Boolean = true
)
