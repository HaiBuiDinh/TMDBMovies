package com.planetmovie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.util.Constants.Companion.MOVIE_NOW_PLAYING_TABLE

@Entity(tableName = MOVIE_NOW_PLAYING_TABLE)
class MovieNowPlayingEntity(
    var movieNowPlayingData: MovieResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}