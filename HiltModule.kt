package com.example.musicstream.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.musicstream.data.local.MusicDatabase
import com.example.musicstream.data.remote.SpotifyApiService
import com.example.musicstream.data.remote.YouTubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {
    
    // ============ SHARED PREFERENCES ============
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("musicstream_prefs", Context.MODE_PRIVATE)
    }
    
    // ============ ROOM DATABASE ============
    @Provides
    @Singleton
    fun provideMusicDatabase(
        @ApplicationContext context: Context
    ): MusicDatabase {
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            "musicstream.db"
        ).build()
    }
    
    // ============ SPOTIFY API ============
    @Provides
    @Singleton
    fun provideSpotifyRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideSpotifyApiService(retrofit: Retrofit): SpotifyApiService {
        return retrofit.create(SpotifyApiService::class.java)
    }
    
    // ============ YOUTUBE API ============
    @Provides
    @Singleton
    fun provideYouTubeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideYouTubeApiService(retrofit: Retrofit): YouTubeApiService {
        return retrofit.create(YouTubeApiService::class.java)
    }
}
