package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.model.MovieResult
import com.example.tmdbmovies.databinding.ItemSearchTvBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemSearchTvAdapter: RecyclerView.Adapter<ItemSearchTvAdapter.MyViewHolder>() {

    private var searchTvResult = emptyList<MovieResult>()

    class MyViewHolder(private val binding: ItemSearchTvBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(searchTvResult: MovieResult) {
            binding.searchTvResult = searchTvResult
            binding.searchTvResult
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchResult = searchTvResult[position]
        holder.bind(searchResult)
    }

    override fun getItemCount() = searchTvResult.size

    fun searchDiffUtil(newData: MovieResponse) {
        val searchTvDiffResult = NetworkDiffUtil(searchTvResult, newData.movieResults)
        val diffUtilResult = DiffUtil.calculateDiff(searchTvDiffResult)
        searchTvResult = newData.movieResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}