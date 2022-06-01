package com.example.tmdbmovies.ui.fragment.movie.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.databinding.FragmentSearchMovieBinding
import com.example.tmdbmovies.ui.SharedViewModel
import com.example.tmdbmovies.ui.adapter.ItemSearchMovieAdapter
import com.example.tmdbmovies.ui.fragment.movie.main.MovieViewModel
import com.example.tmdbmovies.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [SearchMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchMovieFragment : Fragment(R.layout.fragment_search_movie) {
    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mSearchViewModel: MovieViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    //Adapter
    private val mSearchMovieAdapter: ItemSearchMovieAdapter by lazy { ItemSearchMovieAdapter() }

    //Shimmer loading
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchMovieBinding.bind(view)

        initRecyclerView()
        searchMovie()
    }

    private fun initRecyclerView() {
        binding.rvSearchMovie.apply {
            adapter = mSearchMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun searchMovie() {
        binding.svSearchMovie.isSubmitButtonEnabled = true
        binding.svSearchMovie.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    doSearch(query)
                    binding.svSearchMovie.clearFocus()
                    binding.svSearchMovie.hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun doSearch(query: String) {
        val searchQuery = "$query"
        mSearchViewModel.getSearchMovie(mSharedViewModel.searchQueries(searchQuery))
        mSearchViewModel.searchMovie.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    val movieData = response.data
                    movieData?.let { mSearchMovieAdapter.searchDiffUtil(movieData) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun showShimmer(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                isShimmerLoading = true
                shimmerRvSearch.startShimmer()
                shimmerRvSearch.visibility = View.VISIBLE
                rvSearchMovie.visibility = View.INVISIBLE
            } else {
                isShimmerLoading = false
                shimmerRvSearch.stopShimmer()
                shimmerRvSearch.visibility = View.GONE
                rvSearchMovie.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}