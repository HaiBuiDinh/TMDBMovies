package com.example.tmdbmovies.ui.fragment.tv.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.tmdbmovies.data.Repository
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.data.local.entity.TvAiringTodayEntity
import com.example.tmdbmovies.data.local.entity.TvPopularEntity
import com.example.tmdbmovies.data.local.entity.TvTopRatedEntity
import com.example.tmdbmovies.data.remote.model.MovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {
    /**
     * Room variable to read data
     */
    val readTvAiringToday: LiveData<List<TvAiringTodayEntity>> = repository.mLocalData.readTvAiringToday().asLiveData()
    val readTvPopular: LiveData<List<TvPopularEntity>> = repository.mLocalData.readTvPopular().asLiveData()
    val readTvTopRated: LiveData<List<TvTopRatedEntity>> = repository.mLocalData.readTvTopRated().asLiveData()

    /**
     * Insert data to Room
     */
    private fun insertTvAiringToday(tvAiringTodayEntity: TvAiringTodayEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertTvAiringToday(tvAiringTodayEntity)
        }

    private fun insertTvPopular(tvPopularEntity: TvPopularEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertTvPopular(tvPopularEntity)
        }

    private fun insertTvTopRated(tvTopRatedEntity: TvTopRatedEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.mLocalData.insertTvTopRated(tvTopRatedEntity)
        }

    /**
     * Retrofit variable
     */
    var tvAiringToday: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var tvPopular: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var tvTopRated: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    var searchTv: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()

    fun getTvsAiringToday() = viewModelScope.launch {
        tvAiringToday.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getTvAiringToday()
                tvAiringToday.value = handleTvResponse(response)

                val tvAiringTodayData = tvAiringToday.value!!.data
                if (tvAiringTodayData != null) {
                    offlineCacheTvAiringToday(tvAiringTodayData)
                }
            } catch (e: Exception) {
                tvAiringToday.value = Resource.Error("TV Not Found.")
            }
        } else {
            tvAiringToday.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getTvsPopular() = viewModelScope.launch {
        tvPopular.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getTvPopular()
                tvPopular.value = handleTvResponse(response)

                val tvPopularData = tvPopular.value!!.data
                if (tvPopularData != null) {
                    offlineCacheTvPopular(tvPopularData)
                }
            } catch (e: Exception) {
                tvPopular.value = Resource.Error("TV Not Found.")
            }
        } else {
            tvPopular.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getTvsTopRated() = viewModelScope.launch {
        tvTopRated.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val response = repository.mRemoteData.getTvTopRated()
                tvTopRated.value = handleTvResponse(response)

                val tvTopRatedData = tvTopRated.value!!.data
                if(tvTopRatedData != null) {
                    offlineCacheTvTopRated(tvTopRatedData)
                }
            } catch (e: Exception) {
                tvTopRated.value = Resource.Error("TV Not Found.")
            }
        } else {
            tvTopRated.value = Resource.Error("No Internet Connection.")
        }
    }

    fun getSearchTv(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchTv.value = Resource.Loading()
        if (hasConnectivity()) {
            try {
                val responseSearchTv = repository.mRemoteData.getSearchTv(searchQuery)
                searchTv.value = handleTvResponse(responseSearchTv)
            } catch (e: Exception) {
                searchTv.value = Resource.Error("Tv Not Found.")
            }
        } else {
            searchTv.value = Resource.Error("No Internet Connection.")
        }
    }

    /**
     * Caching data online to room
     */

    private fun offlineCacheTvAiringToday(tvAiringTodayData: MovieResponse) {
        val tvAiringTodayEntity = TvAiringTodayEntity(tvAiringTodayData)
        insertTvAiringToday(tvAiringTodayEntity)
    }

    private fun offlineCacheTvPopular(tvPopularData: MovieResponse) {
        val tvPopularEntity = TvPopularEntity(tvPopularData)
        insertTvPopular(tvPopularEntity)
    }

    private fun offlineCacheTvTopRated(tvTopRatedData: MovieResponse) {
        val tvTopRatedEntity = TvTopRatedEntity(tvTopRatedData)
        insertTvTopRated(tvTopRatedEntity)
    }

    /**
     * Handle TV Response
     */
    private fun handleTvResponse(response: Response<MovieResponse>): Resource<MovieResponse> {
        return when {
            response.message().toString().contains("Timeout") -> Resource.Error("TimeOut.")
            response.code() == 402 -> Resource.Error("Api Key Limited.")
            response.body()!!.movieResults.isNullOrEmpty() -> Resource.Error("Tv not found.")
            response.isSuccessful -> {
                val tvData = response.body()
                Resource.Success(tvData)
            }
            else -> Resource.Error(response.message())
        }
    }

    /**
     * Check connect internet
     */
    private fun hasConnectivity(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}