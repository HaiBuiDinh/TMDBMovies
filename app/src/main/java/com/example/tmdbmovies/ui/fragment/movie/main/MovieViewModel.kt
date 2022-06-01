package com.example.tmdbmovies.ui.fragment.movie.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.tmdbmovies.data.Repository
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.data.local.entity.MovieNowPlayingEntity
import com.example.tmdbmovies.data.local.entity.MoviePopularEntity
import com.example.tmdbmovies.data.local.entity.MovieUpcomingEntity
import com.example.tmdbmovies.data.remote.model.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieViewModel
@Inject
constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /**
     * Room variable to read data
     */

    val readMoviesNowPlaying: LiveData<List<MovieNowPlayingEntity>> = repository.mLocalData.readMovieNowPlaying().asLiveData()
    val readMoviesPopular: LiveData<List<MoviePopularEntity>> = repository.mLocalData.readMoviePopular().asLiveData()
    val readMoviesUpcoming: LiveData<List<MovieUpcomingEntity>> = repository.mLocalData.readMovieUpcoming().asLiveData()

    /**
     * Insert data to Room
     */

    private fun insertMoviesNowPlaying(moviesNowPlayingEntity: MovieNowPlayingEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertMovieNowPlaying(moviesNowPlayingEntity)
        }

    private fun insertMoviesPopular(moviesPopularEntity: MoviePopularEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertMoviePopular(moviesPopularEntity)
        }

    private fun insertMoviesUpcoming(moviesUpcomingEntity: MovieUpcomingEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertMovieUpcoming(moviesUpcomingEntity)
        }

    /**
     * Retrofit variable
     */

    var movieNowPlaying: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var moviePopular: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var movieUpcoming: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchMovie: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    fun getMoviesNowPlaying() = viewModelScope.launch {
        movieNowPlaying.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getMovieNowPlaying()
                movieNowPlaying.value = handleMovieResponse(response)
                val movieNowPlayingData = movieNowPlaying.value!!.data
                if (movieNowPlayingData != null) {
                    offlineCacheNowPlaying(movieNowPlayingData)
                }
            } catch (e: Exception) {
                movieNowPlaying.value = Resource.Error("Movie Now Playing not found.")
            }
        } else {
            movieNowPlaying.value = Resource.Error(" No Internet Connection.")
        }
    }

    fun getMoviesPopular() = viewModelScope.launch {
        moviePopular.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getMoviePopular()
                moviePopular.value = handleMovieResponse(response)
                val moviePopularData = moviePopular.value!!.data
                if (moviePopularData != null) {
                    offlineCachePopular(moviePopularData)
                }
            } catch (e: Exception) {
                movieNowPlaying.value = Resource.Error("Movie Popular not found")
            }
        } else {
            moviePopular.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getMoviesUpcoming() = viewModelScope.launch {
        movieUpcoming.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getMovieUpcoming()
                movieUpcoming.value = handleMovieResponse(response)
                val movieUpcomingData = movieUpcoming.value!!.data
                if (movieUpcomingData != null) {
                    offlineCacheUpcoming(movieUpcomingData)
                }
            } catch (e: Exception) {
                movieUpcoming.value = Resource.Error("Movie Upcoming not found.")
            }
        } else {
            movieUpcoming.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getSearchMovie(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchMovie.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getSearchMovie(searchQuery)
                searchMovie.value = handleMovieResponse(response)
            } catch (e: Exception) {
                searchMovie.value = Resource.Error("Movies Not Found.")
            }
        } else {
            searchMovie.value = Resource.Error("No Internet Connection.")
        }
    }

    /**
     * Cache data from internet to the room
     */

    private fun offlineCacheNowPlaying(movieNowPlayingData: MovieResponse) {
        val movieEntity = MovieNowPlayingEntity(movieNowPlayingData)
        insertMoviesNowPlaying(movieEntity)
    }

    private fun offlineCachePopular(moviePopularData: MovieResponse) {
        val movieEntity = MoviePopularEntity(moviePopularData)
        insertMoviesPopular(movieEntity)
    }

    private fun offlineCacheUpcoming(movieUpcomingData: MovieResponse) {
        val movieEntity = MovieUpcomingEntity(movieUpcomingData)
        insertMoviesUpcoming(movieEntity)
    }

    /**
     * Convert retrofit response to resource
     */
    private fun handleMovieResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("Timeout")
            response.code() == 402 -> Resource.Error("Api key limited.")
            response.body()!!.movieResults.isNullOrEmpty() -> Resource.Error("Movie not found.")
            response.isSuccessful -> {
                val movieData = response.body()
                Resource.Success(movieData)
            }
            else -> Resource.Error(response.message())
        }
    }

    /**
     * Check connection
     */
    private fun hasConnectivity(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}