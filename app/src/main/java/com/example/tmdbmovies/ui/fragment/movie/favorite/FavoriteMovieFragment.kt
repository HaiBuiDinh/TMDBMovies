package com.example.tmdbmovies.ui.fragment.movie.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.FragmentFavoriteMovieBinding
import com.example.tmdbmovies.ui.adapter.ItemFavoriteMovieAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FavoriteMovieFragment : Fragment(R.layout.fragment_favorite_movie) {
    //View binding
    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mFavoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var mFavoriteMovieAdapter: ItemFavoriteMovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteMovieBinding.bind(view)

        setHasOptionsMenu(true)
        initRecyclerView()
        initDataBinding()
    }

    private fun initRecyclerView() {
        binding.rvFavoriteMovie.apply {
            mFavoriteMovieAdapter = ItemFavoriteMovieAdapter(requireActivity(), mFavoriteViewModel)
            adapter = mFavoriteMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favMovieViewModel = mFavoriteViewModel
        binding.mFavMovieAdapter = mFavoriteMovieAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mFavoriteMovieAdapter.destroyContextualAction()
    }
}