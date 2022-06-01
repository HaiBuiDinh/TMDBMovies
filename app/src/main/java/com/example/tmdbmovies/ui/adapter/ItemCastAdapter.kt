package com.example.tmdbmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.data.remote.model.MovieCast
import com.example.tmdbmovies.data.remote.model.MovieCredits
import com.example.tmdbmovies.databinding.ItemCastBinding
import com.example.tmdbmovies.util.NetworkDiffUtil

class ItemCastAdapter: RecyclerView.Adapter<ItemCastAdapter.MyViewHolder>() {

    private var movieCast = emptyList<MovieCast>()

    class MyViewHolder(private val binding: ItemCastBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCast: MovieCast) {
            binding.movieCast = movieCast
            binding.executePendingBindings()
            binding.tvCast.isSelected = true
            binding.tvCastCharName.isSelected = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val castDetail = movieCast[position]
        holder.bind(castDetail)
    }

    override fun getItemCount(): Int {
        return movieCast.size
    }

    fun castDiffUtil(newData: MovieCredits) {
        val castDiffUtil = NetworkDiffUtil(movieCast, newData.movieCast)
        val diffUtilResult = DiffUtil.calculateDiff(castDiffUtil)
        movieCast = newData.movieCast
        diffUtilResult.dispatchUpdatesTo(this)
    }
}