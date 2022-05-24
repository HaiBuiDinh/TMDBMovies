package com.example.tmdbmovies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.util.Constants.Companion.TV_POPULAR_TABLE

@Entity(tableName = TV_POPULAR_TABLE)
class TvPopularEntity(
    var tvPopularEntity: MovieResponse
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}