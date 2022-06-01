package com.example.tmdbmovies.ui.fragment.tv.search

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
import com.example.tmdbmovies.databinding.FragmentSearchTvBinding
import com.example.tmdbmovies.ui.SharedViewModel
import com.example.tmdbmovies.ui.adapter.ItemSearchTvAdapter
import com.example.tmdbmovies.ui.fragment.tv.main.TvViewModel
import com.example.tmdbmovies.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [SearchTvFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchTvFragment : Fragment(R.layout.fragment_search_tv) {
    private var _binding: FragmentSearchTvBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mSearchTvViewModel: TvViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    //Adapter
    private val mSearchTvAdapter: ItemSearchTvAdapter by lazy { ItemSearchTvAdapter() }

    //Shimmer
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchTvBinding.bind(view)

        initRecyclerView()
        searchTv()
    }

    private fun initRecyclerView() {
        binding.rvSearchTv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSearchTvAdapter
            setHasFixedSize(true)
        }
    }

    private fun searchTv() {
        binding.svSearchTv.isSubmitButtonEnabled = true
        binding.svSearchTv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    doSearch(query)
                    binding.svSearchTv.clearFocus()
                    binding.svSearchTv.hideKeyboard()
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
        mSearchTvViewModel.getSearchTv(mSharedViewModel.searchQueries(searchQuery))
        mSearchTvViewModel.searchTv.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let {
                        mSearchTvAdapter.searchDiffUtil(response.data)
                    }
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

    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerRvSearch.startShimmer()
                shimmerRvSearch.visibility = View.VISIBLE
                rvSearchTv.visibility = View.INVISIBLE
            } else {
                isShimmerLoading = false
                shimmerRvSearch.stopShimmer()
                shimmerRvSearch.visibility = View.GONE
                rvSearchTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}