package com.example.tmdbmovies.ui.fragment.movie.detail

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.tmdbmovies.data.Repository
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.data.local.entity.MovieFavoriteEntity
import com.example.tmdbmovies.data.remote.model.MovieDetailResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

    /**
     * Room datbse
     */
    val getFavoriteMovie: LiveData<List<MovieFavoriteEntity>> = repository.mLocalData.getFavoriteMovie().asLiveData()

    fun insertFavoriteMovie(movieDetailResponse: MovieDetailResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertFavoriteMovie(movieDetailResponse)
        }

    fun deleteFavoriteMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.deleteFavoriteMovie(movieId)
        }
    }

    /**
     * Retrofit
     */

    var movieDetail: MutableLiveData<Resource<MovieDetailResponse>> = MutableLiveData()

    fun getMovieDetail(queries: Int) = viewModelScope.launch {
        movieDetail.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getMovieDetail(queries)
                movieDetail.value = handleMovieResponse(response)
            } catch (e: Exception) {
                movieDetail.value = Resource.Error("Movie Not Found.")
            }
        } else {
            movieDetail.value = Resource.Error("No Internet Connection.")
        }
    }

    /**
     * Convert retrofit response to resource
     */
    private fun handleMovieResponse(response: Response<MovieDetailResponse>): Resource<MovieDetailResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("Timeout")
            response.code() == 402 -> Resource.Error("Api key limited.")
            response.isSuccessful -> {
                val movieData = response.body()
                Resource.Success(movieData!!)
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