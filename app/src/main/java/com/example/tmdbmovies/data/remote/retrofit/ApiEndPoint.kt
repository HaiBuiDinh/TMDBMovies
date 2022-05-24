package com.example.tmdbmovies.data.remote.retrofit

import com.example.tmdbmovies.data.remote.model.MovieDetailResponse
import com.example.tmdbmovies.data.remote.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.tmdbmovies.util.Constants.*
import com.example.tmdbmovies.util.Constants.Companion.QUERY_API
import com.example.tmdbmovies.util.Constants.Companion.API_KEY
import com.example.tmdbmovies.util.Constants.Companion.QUERY_APPEND_RESPONSE
import com.example.tmdbmovies.util.Constants.Companion.QUERY_MOVIE_ID
import com.example.tmdbmovies.util.Constants.Companion.QUERY_PAGE
import com.example.tmdbmovies.util.Constants.Companion.QUERY_TV_ID
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiEndPoint {

    /**
     * Movies
     */

    @GET("movie/now_playing")
    suspend fun getMovieNowPlaying(
        @Query(QUERY_API) api_key: String = API_KEY,
        @Query(QUERY_PAGE) page:Int = 1
    ): Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getMoviePopular(
        @Query(QUERY_API) api_key: String = API_KEY,
        @Query(QUERY_PAGE) page: Int = 1
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getMovieUpcoming(
        @Query(QUERY_API) api_key: String = API_KEY,
        @Query(QUERY_PAGE) page:Int = 1
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path(QUERY_MOVIE_ID) movieId: Int,
        @Query(QUERY_APPEND_RESPONSE) parameter: String,
        @Query(QUERY_API) api_key: String = API_KEY
    ): Response<MovieDetailResponse>

    /**
     * Search
     */

    @GET("search/movie")
    suspend fun getSearchMovie(
        @QueryMap
        movieSearchQuery: Map<String, String>
    ): Response<MovieResponse>

    @GET("search/tv")
    suspend fun getSearchTv(
        @QueryMap
        tvSearchQuery: Map<String, String>
    ): Response<MovieResponse>

    /**
     * TV
     */

    @GET("tv/popular")
    suspend fun getTvPopular(
        @Query(QUERY_API) api_key: String = API_KEY,
        @Query(QUERY_PAGE) page:Int = 1
    ): Response<MovieResponse>

    @GET("tv/airing_today")
    suspend fun getTvAiringToday(
        @Query(QUERY_API) api_key: String = API_KEY,
        @Query(QUERY_PAGE) page:Int = 1
    ): Response<MovieResponse>

    @GET("tv/top_rated")
    suspend fun getTvTopRated(
        @Query(QUERY_API) apiKey: String = API_KEY,
        @Query(QUERY_PAGE) page: Int = 1
    ): Response<MovieResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path(QUERY_TV_ID) tvId: Int,
        @Query(QUERY_APPEND_RESPONSE) parameter: String,
        @Query(QUERY_API) apiKey: String = API_KEY
    ): Response<MovieDetailResponse>
}