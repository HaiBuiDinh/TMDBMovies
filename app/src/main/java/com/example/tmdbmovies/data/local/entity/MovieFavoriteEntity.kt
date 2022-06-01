package com.example.tmdbmovies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdbmovies.util.Constants.Companion.MOVIE_FAVORITE_TABLE

@Entity(tableName = MOVIE_FAVORITE_TABLE)
class MovieFavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    val overview: String?,
    val backdropPath: String?,
    val posterPath: String?,
    val title: String?,
    val voteAverage: Double?,
    val voteCount: Int?,
    val popularity: Double?,
    val releaseDate: String?,
)