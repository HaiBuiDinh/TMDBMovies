package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.model.MovieResult
import com.example.tmdbmovies.databinding.ItemSearchMovieBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemSearchMovieAdapter: RecyclerView.Adapter<ItemSearchMovieAdapter.MyViewHolder>() {

    private var searchResult = emptyList<MovieResult>()

    class MyViewHolder(private val binding: ItemSearchMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(searchMovieResult: MovieResult) {
            binding.searchMovieResult = searchMovieResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentSearchMovie = searchResult[position]
        holder.bind(currentSearchMovie)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    fun searchDiffUtil(newData: MovieResponse) {
        val searchMovieDiffUtil = NetworkDiffUtil(searchResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchMovieDiffUtil)
        searchResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}