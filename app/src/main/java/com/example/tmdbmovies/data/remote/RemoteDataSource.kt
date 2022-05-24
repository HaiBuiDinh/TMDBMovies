package com.example.tmdbmovies.data.remote

import com.example.tmdbmovies.data.remote.model.MovieDetailResponse
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.retrofit.ApiEndPoint
import com.example.tmdbmovies.util.Constants.Companion.QUERY_APPEND
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiEndpoint: ApiEndPoint) {

    /**
     * Movie
     */

    suspend fun getMoviePopular(): Response<MovieResponse> {
        return apiEndpoint.getMoviePopular()
    }

    suspend fun getMovieNowPlaying(): Response<MovieResponse> {
        return apiEndpoint.getMovieNowPlaying()
    }

    suspend fun getMovieUpcoming(): Response<MovieResponse> {
        return apiEndpoint.getMovieUpcoming()
    }

    suspend fun getMovieDetail(movieDetailId: Int): Response<MovieDetailResponse> {
        return apiEndpoint.getMovieDetail(movieDetailId, QUERY_APPEND)
    }

    suspend fun getSearchMovie(movieSearchQuery: Map<String, String>): Response<MovieResponse> {
        return apiEndpoint.getSearchMovie(movieSearchQuery)
    }

    /**
     * TV
     */

    suspend fun getTvAiringToday(): Response<MovieResponse> {
        return apiEndpoint.getTvAiringToday()
    }

    suspend fun getTvPopular(): Response<MovieResponse> {
        return apiEndpoint.getTvPopular()
    }

    suspend fun getTvTopRated(): Response<MovieResponse> {
        return apiEndpoint.getTvTopRated()
    }

    suspend fun getTvDetail(tvDetailId: Int): Response<MovieDetailResponse> {
        return apiEndpoint.getTvDetail(tvDetailId, QUERY_APPEND)
    }

    suspend fun getSearchTv(tvSearchQuery: Map<String, String>): Response<MovieResponse> {
        return apiEndpoint.getSearchTv(tvSearchQuery)
    }
}