package com.example.tmdbmovies.ui.fragment.tv.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.FragmentFavoriteTvBinding
import com.example.tmdbmovies.ui.adapter.ItemFavoriteTvAdapter
import com.example.tmdbmovies.ui.fragment.movie.favorite.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteTvFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FavoriteTvFragment : Fragment(R.layout.fragment_favorite_tv) {
    //Binding
    private var _binding: FragmentFavoriteTvBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mFavoriteViewModel: FavoriteViewModel by viewModels()

    private lateinit var mFavoriteTvAdapter: ItemFavoriteTvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteTvBinding.bind(view)

        setHasOptionsMenu(true)
        initRecyclerView()
        initDataBinding()
    }

    private fun initRecyclerView() {
        binding.rvFavoriteTv.apply {
            mFavoriteTvAdapter =  ItemFavoriteTvAdapter(requireActivity(), mFavoriteViewModel)
            adapter = mFavoriteTvAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.favTvViewModel = mFavoriteViewModel
        binding.mFavTvAdapter = mFavoriteTvAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}