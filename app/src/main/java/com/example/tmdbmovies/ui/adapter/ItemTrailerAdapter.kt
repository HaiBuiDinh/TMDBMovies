package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.remote.model.MovieVideos
import com.example.tmdbmovies.data.remote.model.MovieVideosResult
import com.example.tmdbmovies.databinding.ItemTrailerBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemTrailerAdapter: RecyclerView.Adapter<ItemTrailerAdapter.MyViewHolder>() {

    private var movieTrailer = emptyList<MovieVideosResult>()

    class MyViewHolder(private val binding: ItemTrailerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movieTrailer: MovieVideosResult) {
            binding.movieTrailer = movieTrailer
            binding.executePendingBindings()
            binding.tvTrailer.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_down_transition_animation)
        val trailerDetail = movieTrailer[position]
        holder.bind(trailerDetail)
    }

    override fun getItemCount(): Int {
        return movieTrailer.size
    }

    fun trailerDiffUtil(newData: MovieVideos) {
        val trailerDiffUtil = NetworkDiffUtil(movieTrailer, newData.movieVideoResults)
        val diffUtilResult = DiffUtil.calculateDiff(trailerDiffUtil)
        movieTrailer = newData.movieVideoResults
        diffUtilResult.dispatchUpdatesTo(this)
    }
}