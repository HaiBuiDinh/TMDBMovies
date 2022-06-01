package com.example.tmdbmovies.data

import com.example.tmdbmovies.data.local.LocalDataSource
import com.example.tmdbmovies.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource
){
    val mRemoteData = remoteDataSource
    val mLocalData = localDataSource
}