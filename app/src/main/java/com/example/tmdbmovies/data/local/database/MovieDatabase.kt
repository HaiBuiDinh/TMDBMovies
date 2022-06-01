package com.example.tmdbmovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tmdbmovies.data.local.entity.*

@Database(entities = [
    MovieFavoriteEntity::class,
    MovieNowPlayingEntity::class,
    MoviePopularEntity::class,
    MovieUpcomingEntity::class,
    TvAiringTodayEntity::class,
    TvFavoriteEntity::class,
    TvPopularEntity::class,
    TvTopRatedEntity::class
], version = 1, exportSchema = false)
@TypeConverters(MovieTypeConverter::class)
abstract class MovieDatabase : RoomDatabase(){
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
}