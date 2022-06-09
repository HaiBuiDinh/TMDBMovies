package com.example.tmdbmovies.ui.adapter

import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.entity.TvFavoriteEntity
import com.example.tmdbmovies.databinding.ItemFavoriteTvBinding
import com.example.tmdbmovies.ui.fragment.movie.favorite.FavoriteViewModel
import com.example.tmdbmovies.ui.fragment.tv.favorite.FavoriteTvFragmentDirections
import com.example.tmdbmovies.util.NetworkDiffUtil
import com.google.android.material.snackbar.Snackbar

class ItemFavoriteTvAdapter(
    private val requiresActivity: FragmentActivity,
    private val favTvViewModel: FavoriteViewModel
) : RecyclerView.Adapter<ItemFavoriteTvAdapter.MyViewHolder>(), ActionMode.Callback {

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var multiSelection = false

    private var selectedTv = arrayListOf<TvFavoriteEntity>()
    private var viewHolder = arrayListOf<MyViewHolder>()
    private var tvFavoriteEntity = emptyList<TvFavoriteEntity>()

    class MyViewHolder(val binding: ItemFavoriteTvBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(tvFavoriteEntity: TvFavoriteEntity) {
            binding.favoriteTvEntity = tvFavoriteEntity
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFavoriteTvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        viewHolder.add(holder)
        rootView = holder.itemView.rootView

        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.left_to_right_animation)
        val currentFavorite = tvFavoriteEntity[position]
        holder.bind(currentFavorite)

        saveItemOnScroll(currentFavorite, holder)

        /** Single click */
        holder.binding.constraintFavoriteTv.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavorite)
            } else {
                val action = FavoriteTvFragmentDirections.actionFavoriteTvFragmentToTvDetailFragment(currentFavorite.id)
                holder.itemView.findNavController().navigate(action)
            }
        }

        /** Long click */
        holder.binding.constraintFavoriteTv.setOnLongClickListener {
            if(!multiSelection) {
                multiSelection = true
                requiresActivity.startActionMode(this)
                applySelection(holder, currentFavorite)
                true
            } else {
                applySelection(holder, currentFavorite)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return tvFavoriteEntity.size
    }

    fun newData(newTvFavorite: List<TvFavoriteEntity>) {
        val tvFavoriteDiffUtil = NetworkDiffUtil(tvFavoriteEntity, newTvFavorite)
        val diffUtilResult = DiffUtil.calculateDiff(tvFavoriteDiffUtil)
        tvFavoriteEntity = newTvFavorite
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
            selectedTv.forEach {
                favTvViewModel.deleteFavoriteTv(it.id)
            }
        }
        showSnackBar("${selectedTv.size} TV/ s Removed.")
        multiSelection = false
        selectedTv.clear()
        mode?.finish()
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        viewHolder.forEach { holder ->
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
        multiSelection = false
        selectedTv.clear()
        applyStatusBarColor(R.color.color_status_bar)
    }

    /** Contextual Action mode */
    private fun saveItemOnScroll(currentTv: TvFavoriteEntity, holder: MyViewHolder) {
        if (selectedTv.contains(currentTv)) {
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
        } else {
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentTv: TvFavoriteEntity) {
        if (selectedTv.contains(currentTv)) {
            selectedTv.remove(currentTv)
            changeMovieStyle(holder, R.color.color_card_unselected, R.color.color_card_unstroke)
            applyActionModeTitle()
        } else {
            selectedTv.add(currentTv)
            changeMovieStyle(holder, R.color.color_card_selected, R.color.color_card_stroke)
            applyActionModeTitle()
        }
    }

    private fun changeMovieStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.constraintFavoriteTv.setBackgroundColor(ContextCompat.getColor(requiresActivity, backgroundColor))
        holder.binding.cvFavoriteTv.strokeColor = ContextCompat.getColor(requiresActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedTv.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> mActionMode.title = "${selectedTv.size} item selected"
            else -> mActionMode.title = "${selectedTv.size} items selected"
        }
    }

    private fun applyStatusBarColor(color: Int) {
        requiresActivity.window.statusBarColor = ContextCompat.getColor(requiresActivity, color)
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