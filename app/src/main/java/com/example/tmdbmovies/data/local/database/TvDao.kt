package com.example.tmdbmovies.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovies.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TvDao {

    /**
     * Insert
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvPopular(tvPopularEntity: TvPopularEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTv(tvFavoriteEntity: TvFavoriteEntity)

    /**
     * Read
     */

    @Query("SELECT * FROM tv_airing_today_table")
    fun readTvAiringToday(): Flow<List<TvAiringTodayEntity>>

    @Query("SELECT * FROM tv_popular_table")
    fun readTvPopular(): Flow<List<TvPopularEntity>>

    @Query("SELECT * FROM tv_top_rated_table")
    fun readTvTopRated(): Flow<List<TvTopRatedEntity>>

    @Query("SELECT * FROM tv_favorite_table")
    fun getFavoriteTv(): Flow<List<TvFavoriteEntity>>

    /**
     * Delete
     */

    @Query("DELETE FROM tv_favorite_table WHERE id =:tvId")
    suspend fun deleteFavoriteTv(tvId: Int)

    @Query("DELETE FROM tv_favorite_table")
    suspend fun deleteAllFavoriteTv()
}