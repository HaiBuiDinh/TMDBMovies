package com.example.tmdbmovies.ui.adapter

import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.entity.MovieFavoriteEntity
import com.example.tmdbmovies.databinding.ItemFavoriteMovieBinding
import com.example.tmdbmovies.ui.fragment.movie.favorite.FavoriteMovieFragmentDirections
import com.example.tmdbmovies.ui.fragment.movie.favorite.FavoriteViewModel
import com.example.tmdbmovies.util.NetworkDiffUtil
import com.google.android.material.snackbar.Snackbar

class ItemFavoriteMovieAdapter(
    private val requireActivity: FragmentActivity,
    private val favMovieViewModel: FavoriteViewModel
): RecyclerView.Adapter<ItemFavoriteMovieAdapter.MyViewHolder>(), ActionMode.Callback {

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false

    private var selectedMovies = arrayListOf<MovieFavoriteEntity>()
    private var viewHolder = arrayListOf<MyViewHolder>()
    private var movieFavoriteEntity = emptyList<MovieFavoriteEntity>()

    class MyViewHolder(val binding: ItemFavoriteMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movieFavoriteEntity: MovieFavoriteEntity) {
            binding.favoriteMovieEntity = movieFavoriteEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        viewHolder.add(holder)
        rootView = holder.itemView.rootView

        val currentFavorite = movieFavoriteEntity[position]
        holder.bind(currentFavorite)

        saveItemOnScroll(currentFavorite, holder)

        /** Single click */
        holder.binding.constraintFavoriteMovie.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavorite)
            } else {
                val action = FavoriteMovieFragmentDirections.actionFavoriteMovieFragmentToMovieDetailFragment(currentFavorite.id)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /** Long click */
        holder.binding.constraintFavoriteMovie.setOnLongClickListener{
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentFavorite)
                true
            } else {
                applySelection(holder, currentFavorite)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return movieFavoriteEntity.size
    }

    fun newData(newMovieFavorite: List<MovieFavoriteEntity>) {
        val movieFavoriteDiffUtil = NetworkDiffUtil(movieFavoriteEntity, newMovieFavorite)
        val diffUtilResult = DiffUtil.calculateDiff(movieFavoriteDiffUtil)
        movieFavoriteEntity = newMovieFavorite
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_favorite_contextual, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.color_status_bar)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.favorite_movie_delete_menu) {
            selectedMovies.forEach {
                favMovieViewModel.deleteFavoriteMovie(it.id)
            }
            showSnackBar("${selectedMovies.size} Movie/ s Removed.")
            multiSelection = false
            selectedMovies.clear()
            mode?.finish()
        }

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        viewHolder.forEach { holder ->
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
        multiSelection = false
        selectedMovies.clear()
        applyStatusBarColor(R.color.color_status_bar)
    }

    /** Contextual Action mode */
    private fun saveItemOnScroll(currentMovie: MovieFavoriteEntity, holder: MyViewHolder) {
        if (selectedMovies.contains(currentMovie)) {
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
        } else {
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentMovie: MovieFavoriteEntity) {
        if (selectedMovies.contains(currentMovie)) {
            selectedMovies.remove(currentMovie)
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
            applyActionModeTitle()
        } else {
            selectedMovies.add(currentMovie)
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
            applyActionModeTitle()
        }
    }

    private fun changeMovieStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.constraintFavoriteMovie.setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))
        holder.binding.cvFavoriteMovie.strokeColor = ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedMovies.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> mActionMode.title = "${selectedMovies.size} item selected"
            else -> mActionMode.title = "${selectedMovies.size} items selected"
        }
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun destroyContextualAction() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("OK."){}.show()
    }
}