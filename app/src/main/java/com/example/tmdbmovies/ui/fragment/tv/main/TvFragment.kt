package com.example.tmdbmovies.ui.fragment.tv.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.databinding.FragmentTvBinding
import com.example.tmdbmovies.ui.SharedViewModel
import com.example.tmdbmovies.ui.adapter.ItemTvListAdapter
import com.example.tmdbmovies.util.NetworkListener
import com.example.tmdbmovies.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [TvFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class TvFragment : Fragment(R.layout.fragment_tv) {
    private var _binding: FragmentTvBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mTvViewModel: TvViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    //Adapter
    private lateinit var mAiringTodayAdapter: ItemTvListAdapter
    private lateinit var mPopularAdapter: ItemTvListAdapter
    private lateinit var mTopRatedAdapter: ItemTvListAdapter

    //Network Listener
    private lateinit var networkListener: NetworkListener

    //Shimmer
    private var isShimmerLoading: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mTvViewModel = mTvViewModel

        initRecyclerView()
        readBackOnline()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun initRecyclerView() {
        binding.rvAiringNow.apply {
            mAiringTodayAdapter = ItemTvListAdapter()
            adapter = mAiringTodayAdapter
            setHasFixedSize(true)
        }

        binding.rvPopularTv.apply {
            mPopularAdapter = ItemTvListAdapter()
            adapter = mPopularAdapter
            setHasFixedSize(true)
        }

        binding.rvTopRatedTv.apply {
            mTopRatedAdapter = ItemTvListAdapter()
            adapter = mTopRatedAdapter
            setHasFixedSize(true)
        }
    }

    private fun readBackOnline() {
        mSharedViewModel.readBackOnline.observe(viewLifecycleOwner) {
            mSharedViewModel.backOnline = it
        }
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collectLatest { status ->
                mSharedViewModel.networkStatus = status
                readDatabase()
            }
        }
    }

    private fun readDatabase() {
        lifecycleScope.launchWhenStarted {
            mTvViewModel.readTvAiringToday.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAiringTodayAdapter.tvDiffUtil(database[0].tvAiringTodayData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }

            mTvViewModel.readTvPopular.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.tvDiffUtil(database[0].tvPopularEntity)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }

            mTvViewModel.readTvTopRated.observeOnce(viewLifecycleOwner) { databse ->
                if (databse.isNotEmpty()) {
                    mTopRatedAdapter.tvDiffUtil(databse[0].tvTopRatedData)
                    showShimmer(false)
                } else {
                    readApiResponse()
                }
            }
        }
    }

    private fun readApiResponse() {
        mTvViewModel.getTvsAiringToday()
        mTvViewModel.tvAiringToday.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mAiringTodayAdapter.tvDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }

        mTvViewModel.getTvsPopular()
        mTvViewModel.tvPopular.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mPopularAdapter.tvDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }

        mTvViewModel.getTvsTopRated()
        mTvViewModel.tvTopRated.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    showShimmer(false)
                    response.data?.let { mTopRatedAdapter.tvDiffUtil(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    hideText()
                    loadDataFromCache()
                }
                is Resource.Loading -> {
                    showShimmer(true)
                }
            }
        }
    }

    private fun showShimmer(isShowShimmer: Boolean) {
        binding.apply {
            if (isShowShimmer) {
                isShimmerLoading = true
                shimmerRvTv.startShimmer()
                shimmerRvTv.visibility = View.VISIBLE
                tvTvOne.visibility = View.GONE
                tvTvTwo.visibility = View.GONE
                tvTvThree.visibility = View.GONE
                rvAiringNow.visibility = View.GONE
                rvPopularTv.visibility = View.GONE
                rvTopRatedTv.visibility = View.GONE
            } else {
                isShimmerLoading = false
                shimmerRvTv.stopShimmer()
                shimmerRvTv.visibility = View.GONE
                tvTvOne.visibility = View.VISIBLE
                tvTvTwo.visibility = View.VISIBLE
                tvTvThree.visibility = View.VISIBLE
                rvAiringNow.visibility = View.VISIBLE
                rvPopularTv.visibility = View.VISIBLE
                rvTopRatedTv.visibility = View.VISIBLE
            }
        }
    }

    private fun hideText() {
        binding.apply {
            tvTvOne.visibility = View.GONE
            tvTvTwo.visibility = View.GONE
            tvTvThree.visibility = View.GONE
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mTvViewModel.readTvAiringToday.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAiringTodayAdapter.tvDiffUtil(database[0].tvAiringTodayData)
                }
            }

            mTvViewModel.readTvPopular.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mPopularAdapter.tvDiffUtil(database[0].tvPopularEntity)
                }
            }

            mTvViewModel.readTvTopRated.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mTopRatedAdapter.tvDiffUtil(database[0].tvTopRatedData)
                }
            }
        }
    }

//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}