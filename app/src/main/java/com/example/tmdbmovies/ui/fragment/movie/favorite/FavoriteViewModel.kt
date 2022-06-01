package com.example.tmdbmovies.ui.fragment.movie.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovies.data.Repository
import com.example.tmdbmovies.data.local.entity.MovieFavoriteEntity
import com.example.tmdbmovies.data.local.entity.TvFavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    val getFavoriteMovie: LiveData<List<MovieFavoriteEntity>> = repository.mLocalData.getFavoriteMovie().asLiveData()
    val getFavoriteTv: LiveData<List<TvFavoriteEntity>> = repository.mLocalData.getFavoriteTv().asLiveData()

    fun deleteFavoriteMovie(movieId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.mLocalData.deleteFavoriteMovie(movieId)
    }

    fun deleteFavoriteTv(tvId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.mLocalData.deleteFavoriteTv(tvId)
    }
}