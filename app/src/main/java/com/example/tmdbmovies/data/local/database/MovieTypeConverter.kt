package com.example.tmdbmovies.data.local.database

import androidx.room.TypeConverter
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun movieDataToString(movieData: MovieResponse) : String {
        return gson.toJson(movieData)
    }

    @TypeConverter
    fun stringToMovieData(data: String): MovieResponse {
        val listType = object : TypeToken<MovieResponse>() {}.type
        return gson.fromJson(data, listType)
    }
}