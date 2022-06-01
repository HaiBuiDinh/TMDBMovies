package com.example.tmdbmovies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.util.Constants.Companion.MOVIE_POPULAR_TABLE

@Entity(tableName = MOVIE_POPULAR_TABLE)
class MoviePopularEntity(
    var moviePopularData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}