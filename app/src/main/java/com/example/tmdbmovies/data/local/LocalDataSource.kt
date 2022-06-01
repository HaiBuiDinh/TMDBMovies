package com.example.tmdbmovies.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovies.data.local.database.MovieDao
import com.example.tmdbmovies.data.local.database.TvDao
import com.example.tmdbmovies.data.local.entity.*
import com.example.tmdbmovies.data.remote.model.MovieDetailResponse
import com.example.tmdbmovies.util.mapMovieDetailToMovieFavoriteEntity
import com.example.tmdbmovies.util.mapTvDetailToTvFavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val tvDao: TvDao
) {
    /**
     * ===========Movie=============
     */

    /**
     * Insert
     */

    suspend fun insertMovieNowPlaying(movieNowPlayingEntity: MovieNowPlayingEntity){
        movieDao.insertMovieNowPlaying(movieNowPlayingEntity = movieNowPlayingEntity)
    }

    suspend fun insertMoviePopular(moviePopularEntity: MoviePopularEntity) {
        movieDao.insertMoviePopular(moviePopularEntity = moviePopularEntity)
    }

    suspend fun insertMovieUpcoming(movieUpcomingEntity: MovieUpcomingEntity) {
        movieDao.insertMovieUpcoming(movieUpcomingEntity = movieUpcomingEntity)
    }

    suspend fun insertFavoriteMovie(movieDetailResponse: MovieDetailResponse) {
        movieDao.insertFavoriteMovie(mapMovieDetailToMovieFavoriteEntity(movieDetailResponse))
    }

    /**
     * Read
     */

    fun readMovieNowPlaying(): Flow<List<MovieNowPlayingEntity>> {
        return movieDao.readMovieNowPlaying()
    }

    fun readMoviePopular(): Flow<List<MoviePopularEntity>> {
        return movieDao.readMoviePopular()
    }

    fun readMovieUpcoming(): Flow<List<MovieUpcomingEntity>> {
        return movieDao.readMovieUpcoming()
    }

    fun getFavoriteMovie(): Flow<List<MovieFavoriteEntity>> {
        return movieDao.getFavoriteMovie()
    }

    /**
     * Delete
     */

    suspend fun deleteFavoriteMovie(movieId: Int) {
        movieDao.deleteFavoriteMovie(movieId = movieId)
    }

    suspend fun deleteAllFavoriteMovie() {
        movieDao.deleteAllFavoriteMovie()
    }

    /**
     * ============Tv==============
     */

    /**
     * Insert
     */
    suspend fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity) {
        tvDao.insertTvAiringToday(tvAiringTodayEntity = tvAiringTodayEntity)
    }

    suspend fun insertTvPopular(tvPopularEntity: TvPopularEntity) {
        tvDao.insertTvPopular(tvPopularEntity)
    }

    suspend fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity) {
        tvDao.insertTvTopRated(tvTopRatedEntity)
    }

    suspend fun insertFavoriteTv(tvDetailResponse: MovieDetailResponse) {
        tvDao.insertFavoriteTv(mapTvDetailToTvFavoriteEntity(tvDetailResponse))
    }

    /**
     * Read
     */

    fun readTvAiringToday(): Flow<List<TvAiringTodayEntity>> {
        return tvDao.readTvAiringToday()
    }

    fun readTvPopular(): Flow<List<TvPopularEntity>> {
        return tvDao.readTvPopular()
    }

    fun readTvTopRated(): Flow<List<TvTopRatedEntity>> {
        return tvDao.readTvTopRated()
    }

    fun getFavoriteTv(): Flow<List<TvFavoriteEntity>> {
        return tvDao.getFavoriteTv()
    }

    /**
     * Delete
     */
    suspend fun deleteFavoriteTv(tvId: Int) {
        tvDao.deleteFavoriteTv(tvId)
    }

    suspend fun deleteAllFavoriteTv() {
        tvDao.deleteAllFavoriteTv()
    }
}