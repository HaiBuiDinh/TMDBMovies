package com.example.tmdbmovies.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieVideos(
    @SerializedName("results") val movieVideoResults: List<MovieVideosResult>
)
