package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.model.MovieResult
import com.example.tmdbmovies.databinding.ItemListTvBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemTvListAdapter: RecyclerView.Adapter<ItemTvListAdapter.MyViewMHolder>() {

    private var tvResult = emptyList<MovieResult>()

    class MyViewMHolder(private val binding: ItemListTvBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tvResult: MovieResult) {
            binding.tvResult = tvResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewMHolder {
        val binding = ItemListTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewMHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewMHolder, position: Int) {
        val currentTv = tvResult[position]
        holder.bind(currentTv)
    }

    override fun getItemCount(): Int {
        return tvResult.size
    }

    fun tvDiffUtil(newData: MovieResponse) {
        val tvDiffUtil = NetworkDiffUtil(tvResult, newData.movieResults)
        val diffResult = DiffUtil.calculateDiff(tvDiffUtil)
        tvResult = newData.movieResults
        diffResult.dispatchUpdatesTo(this)
    }
}