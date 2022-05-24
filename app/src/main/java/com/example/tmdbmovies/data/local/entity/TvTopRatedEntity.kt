package com.example.tmdbmovies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.util.Constants.Companion.TV_TOP_RATED_TABLE

@Entity(tableName = TV_TOP_RATED_TABLE)
class TvTopRatedEntity(
    var tvTopRatedData: MovieResponse
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}