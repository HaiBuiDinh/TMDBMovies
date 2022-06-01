package com.example.tmdbmovies.ui.fragment.movie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.data.remote.model.MovieDetailResponse
import com.example.tmdbmovies.databinding.FragmentMovieDetailBinding
import com.example.tmdbmovies.ui.adapter.ItemCastAdapter
import com.example.tmdbmovies.ui.adapter.ItemTrailerAdapter
import com.example.tmdbmovies.util.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val mMovieDetailViewModel: MovieDetailViewModel by viewModels()

    //Adapter
    private val mItemCastAdapter: ItemCastAdapter by lazy { ItemCastAdapter() }
    private val mItemTrailerAdapter: ItemTrailerAdapter by lazy { ItemTrailerAdapter() }

    //Favorite Movie
    private var movieSaved = false
    private var savedMovieId = 0

    //Shimmer Loading
    private var isShimmerLoading: Boolean = false

    //Navigation Args
    private val args by navArgs<MovieDetailFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        initRecyclerView()
        observeViewModel()
        checkSavedFavoriteMovies()
    }
    
    private fun initRecyclerView() {
        binding.rvDtlCast.apply { 
            adapter = mItemCastAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        
        binding.rvDtlTrailer.apply {
            adapter = mItemTrailerAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observeViewModel() {
        val movieId: Int = args.movieId
        mMovieDetailViewModel.getMovieDetail(movieId)
        mMovieDetailViewModel.movieDetail.observe(viewLifecycleOwner) { detailMovie ->
            when (detailMovie) {
                is Resource.Success -> {
                    showShimmer(false)
                    detailMovie.data?.let { movieDetailResponse(it) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                }
                is Resource.Loading -> showShimmer(true)
            }
        }
    }

    private fun movieDetailResponse(data: MovieDetailResponse) {
        data.let { detailResponse ->
            binding.apply {
                val rating = "${detailResponse.voteAverage} / 10"
                rbDtlOne.rating = detailResponse.voteAverage.div(2).toFloat()
                tvDtlDate.text = detailResponse.releaseDate
                tvDtlRate.text = rating
                ivDtlMovieBackdrop.load(Constants.BASE_IMG_URL_BACKDROP + detailResponse.backdropPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                ivDtlPoster.load(Constants.BASE_IMG_URL_POSTER + detailResponse.posterPath) {
                    crossfade(300)
                    error(R.drawable.ic_no_image)
                }
                if (detailResponse.overview.isNotEmpty()) {
                    tvDtlOverview.text = detailResponse.overview
                } else tvDtlOverview.text = getString(R.string.overview_not_available)
                if (detailResponse.movieCredits.movieCast.isNotEmpty()) {
                    mItemCastAdapter.castDiffUtil(detailResponse.movieCredits)
                } else tvCast.visibility = View.GONE
                if (detailResponse.movieVideos.movieVideoResults.isNotEmpty()) {
                    mItemTrailerAdapter.trailerDiffUtil(detailResponse.movieVideos)
                } else tvTrailer.visibility = View.GONE

                //Favorite
                binding.fabMovieDetail.setOnClickListener{
                    movieSaved = if (!movieSaved) {
                        mMovieDetailViewModel.insertFavoriteMovie(detailResponse)
                        showSnackBarFavorite("Movie Saved.")
                        changeFabColor(R.color.color_icon_favorite)
                        true
                    } else {
                        mMovieDetailViewModel.deleteFavoriteMovie(detailResponse.id)
                        showSnackBarFavorite("Movie Removed.")
                        changeFabColor(R.color.color_icon_unfavorite)
                        false
                    }
                }
            }
        }
    }

    private fun checkSavedFavoriteMovies() {
        mMovieDetailViewModel.getFavoriteMovie.observe(viewLifecycleOwner) { favoriteMoviesEntity ->
            try {
                for (saveMovie in favoriteMoviesEntity) {
                    if (saveMovie.id == args.movieId) {
                        changeFabColor(R.color.color_icon_favorite)
                        savedMovieId = saveMovie.id
                        movieSaved = true
                    }
                }
            } catch (e: Exception) {}
        }
    }

    private fun changeFabColor(color: Int) {
        binding.fabMovieDetail.supportImageTintList = ContextCompat.getColorStateList(requireContext(), color)
    }

    private fun showSnackBarFavorite(message: String) {
        Snackbar.make(
            binding.movieDetailLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") { }.show()
    }


    private fun showShimmer(boolean: Boolean) {
        binding.apply {
            if (boolean) {
                isShimmerLoading = true
                shimmerDetailMovie.startShimmer()
                shimmerDetailMovie.visibility = View.VISIBLE
                fabMovieDetail.visibility = View.GONE
                ivDtlMovieBackdrop.visibility = View.GONE
                shadowBackdrop.visibility = View.GONE
                ivDtlPoster.visibility = View.GONE
                tvRating.visibility = View.GONE
                tvDtlRate.visibility = View.GONE
                rbDtlOne.visibility = View.GONE
                tvReleaseDate.visibility = View.GONE
                tvDtlDate.visibility = View.GONE
                tvOverview.visibility = View.GONE
                tvDtlOverview.visibility = View.GONE
                tvCast.visibility = View.GONE
                rvDtlCast.visibility = View.GONE
                tvTrailer.visibility = View.GONE
                rvDtlTrailer.visibility = View.GONE
            } else {
                isShimmerLoading = false
                shimmerDetailMovie.stopShimmer()
                shimmerDetailMovie.visibility = View.GONE
                fabMovieDetail.visibility = View.VISIBLE
                ivDtlMovieBackdrop.visibility = View.VISIBLE
                shadowBackdrop.visibility = View.VISIBLE
                ivDtlPoster.visibility = View.VISIBLE
                tvRating.visibility = View.VISIBLE
                tvDtlRate.visibility = View.VISIBLE
                rbDtlOne.visibility = View.VISIBLE
                tvReleaseDate.visibility = View.VISIBLE
                tvDtlDate.visibility = View.VISIBLE
                tvOverview.visibility = View.VISIBLE
                tvDtlOverview.visibility = View.VISIBLE
                tvCast.visibility = View.VISIBLE
                rvDtlCast.visibility = View.VISIBLE
                tvTrailer.visibility = View.VISIBLE
                rvDtlTrailer.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}