package com.example.musicstream.data.local

import androidx.room.*
import com.example.musicstream.data.model.*
import kotlinx.coroutines.flow.Flow

// ============ DATABASE ============
@Database(
    entities = [Track::class, Artist::class, Playlist::class, PlaylistTrack::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun artistDao(): ArtistDao
    abstract fun playlistDao(): PlaylistDao
}

// ============ TRACK DAO ============
@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<Track>)
    
    @Query("SELECT * FROM tracks")
    fun getAllTracksFlow(): Flow<List<Track>>
    
    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<Track>
    
    @Query("SELECT * FROM tracks WHERE id = :trackId")
    suspend fun getTrackById(trackId: String): Track?
    
    @Query("SELECT * FROM tracks WHERE artist = :artist")
    fun getTracksByArtist(artist: String): Flow<List<Track>>
    
    @Query("SELECT * FROM tracks WHERE album = :album")
    fun getTracksByAlbum(album: String): Flow<List<Track>>
    
    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrack(trackId: String)
    
    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun getTrackCount(): Int
}

// ============ ARTIST DAO ============
@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: Artist)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<Artist>)
    
    @Query("SELECT * FROM artists")
    fun getAllArtistsFlow(): Flow<List<Artist>>
    
    @Query("SELECT * FROM artists WHERE id = :artistId")
    suspend fun getArtistById(artistId: String): Artist?
    
    @Query("SELECT * FROM artists ORDER BY popularity DESC LIMIT :limit")
    fun getTopArtists(limit: Int = 20): Flow<List<Artist>>
    
    @Query("DELETE FROM artists WHERE id = :artistId")
    suspend fun deleteArtist(artistId: String)
}

// ============ PLAYLIST DAO ============
@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<Playlist>)
    
    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsFlow(): Flow<List<Playlist>>
    
    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<Playlist>
    
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: String): Playlist?
    
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: String)
    
    @Transaction
    @Query("""
        SELECT t.* FROM tracks t
        INNER JOIN playlist_tracks pt ON t.id = pt.trackId
        WHERE pt.playlistId = :playlistId
        ORDER BY pt.addedAt DESC
    """)
    fun getPlaylistTracks(playlistId: String): Flow<List<Track>>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(playlistTrack: PlaylistTrack) {
        // This is used by addTrackToPlaylist method below
    }
    
    suspend fun addTrackToPlaylist(playlistId: String, trackId: String) {
        addTrackToPlaylist(PlaylistTrack(playlistId = playlistId, trackId = trackId))
    }
    
    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String)
    
    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackCount(playlistId: String): Int
}
