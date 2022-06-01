package com.example.tmdbmovies.util

import com.example.tmdbmovies.data.local.entity.MovieFavoriteEntity
import com.example.tmdbmovies.data.local.entity.TvFavoriteEntity
import com.example.tmdbmovies.data.remote.model.MovieDetailResponse

fun mapMovieDetailToMovieFavoriteEntity(movieDetailResponse: MovieDetailResponse): MovieFavoriteEntity {
    return MovieFavoriteEntity(
        movieDetailResponse.id,
        movieDetailResponse.overview,
        movieDetailResponse.backdropPath,
        movieDetailResponse.posterPath,
        movieDetailResponse.title,
        movieDetailResponse.voteAverage,
        movieDetailResponse.voteCount,
        movieDetailResponse.popularity,
        movieDetailResponse.releaseDate
    )
}

fun mapTvDetailToTvFavoriteEntity(movieDetailResponse: MovieDetailResponse): TvFavoriteEntity {
    return TvFavoriteEntity (
        movieDetailResponse.id,
        movieDetailResponse.overview,
        movieDetailResponse.backdropPath,
        movieDetailResponse.posterPath,
        movieDetailResponse.tvName,
        movieDetailResponse.voteAverage,
        movieDetailResponse.voteCount,
        movieDetailResponse.popularity,
        movieDetailResponse.tvFirstAirDate
    )
}