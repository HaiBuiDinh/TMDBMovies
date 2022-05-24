package com.example.tmdbmovies.data.remote.model

import com.google.gson.annotations.SerializedName
import com.planetmovie.data.remote.model.MovieCredits
import com.planetmovie.data.remote.model.MovieGenre

data class MovieDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("videos") val movieVideos: MovieVideos,
    @SerializedName("credits") val movieCredits: MovieCredits,
    @SerializedName("genres") val movieGenres: List<MovieGenre>,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,

    )
