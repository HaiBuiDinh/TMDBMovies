package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.model.MovieResult
import com.example.tmdbmovies.databinding.ItemListMovieBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemMovieListAdapter: RecyclerView.Adapter<ItemMovieListAdapter.MyViewHolder>() {

    private var movieResult = emptyList<MovieResult>()

    class MyViewHolder(private val binding: ItemListMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult: MovieResult) {
            binding.movieResult = movieResult
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_down_transition_animation)
        var currentMovie = movieResult[position]
        holder.bind(currentMovie)
    }

    override fun getItemCount(): Int {
        return movieResult.size
    }

    fun movieDiffUtil(newData: MovieResponse) {
        val moviesDiffUtil = NetworkDiffUtil(movieResult, newData.movieResults)
        val diffResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movieResult = newData.movieResults
        diffResult.dispatchUpdatesTo(this) //gọi adapter và thông báo về views được cập nhật
    }

}