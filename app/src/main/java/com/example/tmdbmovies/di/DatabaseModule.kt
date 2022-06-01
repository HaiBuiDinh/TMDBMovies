package com.example.tmdbmovies.di

import android.content.Context
import androidx.room.Room
import com.example.tmdbmovies.data.local.database.MovieDatabase
import com.example.tmdbmovies.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MovieDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun moviesDao(database: MovieDatabase) = database.movieDao()

    @Singleton
    @Provides
    fun tvDao(database: MovieDatabase) = database.tvDao()

}