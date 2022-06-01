package com.example.tmdbmovies.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovies.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    /**
     * Insert
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieNowPlaying(movieNowPlayingEntity: MovieNowPlayingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePopular(moviePopularEntity: MoviePopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieUpcoming(movieUpcomingEntity: MovieUpcomingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(movieFavoriteEntity: MovieFavoriteEntity)

    /**
     * Read
     */

    @Query("SELECT * FROM movie_now_playing_table")
    fun readMovieNowPlaying(): Flow<List<MovieNowPlayingEntity>>

    @Query("SELECT * FROM movie_popular_table")
    fun readMoviePopular(): Flow<List<MoviePopularEntity>>

    @Query("SELECT * FROM movie_upcoming_table")
    fun readMovieUpcoming(): Flow<List<MovieUpcomingEntity>>

    @Query("SELECT * FROM movie_favorite_table")
    fun getFavoriteMovie(): Flow<List<MovieFavoriteEntity>>

    /**
     * Delete
     */

    @Query("DELETE FROM movie_favorite_table WHERE id =:movieId")
    suspend fun deleteFavoriteMovie(movieId: Int)

    @Query("DELETE FROM movie_favorite_table")
    suspend fun deleteAllFavoriteMovie()
}