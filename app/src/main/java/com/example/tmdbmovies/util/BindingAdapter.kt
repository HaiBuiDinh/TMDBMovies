package com.example.tmdbmovies.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.tmdbmovies.R
import com.example.tmdbmovies.data.local.Resource
import com.example.tmdbmovies.data.local.entity.*
import com.example.tmdbmovies.data.remote.model.MovieCast
import com.example.tmdbmovies.data.remote.model.MovieResponse
import com.example.tmdbmovies.data.remote.model.MovieResult
import com.example.tmdbmovies.data.remote.model.MovieVideosResult
import com.example.tmdbmovies.ui.adapter.ItemFavoriteMovieAdapter
import com.example.tmdbmovies.ui.adapter.ItemFavoriteTvAdapter
import com.example.tmdbmovies.ui.fragment.movie.main.MovieFragmentDirections
import com.example.tmdbmovies.ui.fragment.movie.search.SearchMovieFragmentDirections
import com.example.tmdbmovies.ui.fragment.tv.main.TvFragmentDirections
import com.example.tmdbmovies.ui.fragment.tv.search.SearchTvFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Response

class BindingAdapter {
    companion object {

        @BindingAdapter("navigateMovieToDetail")
        @JvmStatic
        fun navigateMovieToDetail(view: CardView, movieId: Int) {
            view.setOnClickListener {
                val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movieId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("navigateMovieToFavoriteMovie")
        @JvmStatic
        fun navigateMovieToFavoriteMovie(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                val action = MovieFragmentDirections.actionMovieFragmentToFavoriteMovieFragment()
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("navigateMovieToSearch")
        @JvmStatic
        fun navigateMovieToSearch(view: ImageView, navigate: Boolean) {
            view.setOnClickListener {
                val action = MovieFragmentDirections.actionMovieFragmentToSearchFragment()
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("posterPath")
        @JvmStatic
        fun posterPath(view: ImageView, movieResult: MovieResult) {
            view.load(Constants.BASE_IMG_URL_POSTER + movieResult.posterPath) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter(
            "movie_now_playing_response",
            "movie_popular_response",
            "movie_upcoming_response",
            "movie_now_playing_entity",
            "movie_popular_entity",
            "movie_upcoming_entity",
            requireAll = true
        )
        @JvmStatic
        fun movieNoInternetConnection(
            view: View,
            movieNowPlayingResponse: Resource<MovieResponse>?,
            moviePopularResponse: Resource<MovieResponse>?,
            movieUpcomingResponse: Resource<MovieResponse>?,
            movieNowPlayingEntity: List<MovieNowPlayingEntity>?,
            moviePopularEntity: List<MoviePopularEntity>?,
            movieUpcomingEntity: List<MovieUpcomingEntity>?
        ) {
            when(view) {
                is ImageView -> {
                    view.isVisible = movieNowPlayingResponse is Resource.Error && movieNowPlayingEntity.isNullOrEmpty()
                                    || moviePopularResponse is Resource.Error && moviePopularEntity.isNullOrEmpty()
                                    || movieUpcomingResponse is Resource.Error && movieUpcomingEntity.isNullOrEmpty()
                }

                is TextView -> {
                    view.isVisible = movieNowPlayingResponse is Resource.Error && movieNowPlayingEntity.isNullOrEmpty()
                                    || moviePopularResponse is Resource.Error && moviePopularEntity.isNullOrEmpty()
                                    || movieUpcomingResponse is Resource.Error && movieUpcomingEntity.isNullOrEmpty()
                    view.text = movieNowPlayingResponse?.message.toString()
                }
            }
        }

        @BindingAdapter("navigateTvToDetail")
        @JvmStatic
        fun navigateTvToDetail(view: CardView, tvId: Int) {
            view.setOnClickListener {
                val action = TvFragmentDirections.actionTvFragmentToTvDetailFragment(tvId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("navigateTvToFavoriteTv")
        @JvmStatic
        fun navigateTvToFavoriteTv(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                val action = TvFragmentDirections.actionTvFragmentToFavoriteTvFragment()
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("navigateTvToSearch")
        @JvmStatic
        fun navigateTvToSearch(view: ImageView, navigate: Boolean) {
            view.setOnClickListener {
                val action = TvFragmentDirections.actionTvFragmentToSearchTvFragment()
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter(
            "tv_airing_today_response",
            "tv_top_rated_response",
            "tv_popular_response",
            "tv_airing_today_entity",
            "tv_top_rated_entity",
            "tv_popular_entity",
            requireAll = true
        )
        @JvmStatic
        fun tvNoInternetConnection(
            view: View,
            tvAiringTodayResponse: Resource<MovieResponse>?,
            tvTopRatedResponse: Resource<MovieResponse>?,
            tvPopularResponse: Resource<MovieResponse>?,
            tvAiringTodayEntity: List<TvAiringTodayEntity>?,
            tvTopRatedEntity: List<TvTopRatedEntity>?,
            tvPopularEntity: List<TvPopularEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = tvAiringTodayResponse is Resource.Error && tvAiringTodayEntity.isNullOrEmpty()
                                    || tvTopRatedResponse is Resource.Error && tvTopRatedEntity.isNullOrEmpty()
                                    || tvPopularResponse is Resource.Error && tvPopularEntity.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = tvAiringTodayResponse is Resource.Error && tvAiringTodayEntity.isNullOrEmpty()
                                    || tvTopRatedResponse is Resource.Error && tvTopRatedEntity.isNullOrEmpty()
                                    || tvPopularResponse is Resource.Error && tvPopularEntity.isNullOrEmpty()
                    view.text = tvAiringTodayResponse?.message.toString()
                }
            }
        }

        /**
         * Binding for Search
         */

        @BindingAdapter("navigateSearchMovieToDetail")
        @JvmStatic
        fun navigateSearchMovieToDetail(view: CardView, movieId: Int) {
            view.setOnClickListener {
                val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(movieId)
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("navigateSearchTvToDetail")
        @JvmStatic
        fun navigateSearchTvToDetail(view: CardView, tvId: Int) {
            view.setOnClickListener {
                val action = SearchTvFragmentDirections.actionSearchTvFragmentToTvDetailFragment(tvId)
                view.findNavController().navigate(action)
            }
        }

        /**
         * Cast and Trailer
         */

        @BindingAdapter("castPoster")
        @JvmStatic
        fun castPoster(imageView: ImageView, movieResult: MovieCast) {
            imageView.load(Constants.BASE_IMG_URL_CAST + movieResult.profilePath) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("trailerCLick")
        @JvmStatic
        fun trailerCLick(view: CardView, trailerResult: MovieVideosResult) {
            view.setOnClickListener {
                val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_TRAILER_URL_APP + trailerResult.key))
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_TRAILER_URL + trailerResult.key))
                try {
                    it.context?.startActivity(appIntent)
                } catch (ex: ActivityNotFoundException) {
                    it.context?.startActivity(webIntent)
                }
            }
        }

        @BindingAdapter("trailerPoster")
        @JvmStatic
        fun trailerPoster(imageView: ImageView, trailerResult: MovieVideosResult) {
            imageView.load(Constants.BASE_TRAILER_THUMBNAIL + trailerResult.key + Constants.BASE_TRAILER_THUMBNAIL_END) {
                crossfade(true)
                error(R.drawable.ic_no_image)
            }
        }

        /**
         * Favorite
         */
        @BindingAdapter("favMoviePosterPath")
        @JvmStatic
        fun favMoviePosterPath(view: ImageView, favoriteMovieEntity: MovieFavoriteEntity) {
            view.load(Constants.BASE_IMG_URL_POSTER + favoriteMovieEntity.posterPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter("favTvPosterPath")
        @JvmStatic
        fun favTvPosterPath(view: ImageView, favoriteTvEntity: TvFavoriteEntity) {
            view.load(Constants.BASE_IMG_URL_POSTER + favoriteTvEntity.posterPath) {
                crossfade(300)
                error(R.drawable.ic_no_image)
            }
        }

        @BindingAdapter(
            "no_favorite_movie_data",
            "favorite_movie_data",
            requireAll = false
        )
        @JvmStatic
        fun setFavoriteViewVisibility(
            view: View,
            movieFavoriteEntity: List<MovieFavoriteEntity>?,
            mAdapter: ItemFavoriteMovieAdapter?
        ) {
            if (movieFavoriteEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> view.visibility = View.VISIBLE
                    is TextView -> view.visibility = View.VISIBLE
                    is RecyclerView -> view.visibility = View.INVISIBLE
                }
            } else {
                when (view) {
                    is ImageView -> view.visibility = View.INVISIBLE
                    is TextView -> view.visibility = View.INVISIBLE
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.newData(movieFavoriteEntity)
                    }
                }
            }
        }

        @BindingAdapter(
            "no_favorite_tv_data",
            "favorite_tv_data",
            requireAll = false
        )
        @JvmStatic
        fun setFavoriteTvVisibility(
            view: View,
            tvFavoriteEntity: List<TvFavoriteEntity>?,
            mAdapter: ItemFavoriteTvAdapter?
        ) {
            if (tvFavoriteEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> view.visibility = View.VISIBLE
                    is TextView -> view.visibility = View.VISIBLE
                    is RecyclerView -> view.visibility = View.INVISIBLE
                }
            } else {
                when (view) {
                    is ImageView -> view.visibility = View.INVISIBLE
                    is TextView -> view.visibility = View.INVISIBLE
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.newData(tvFavoriteEntity)
                    }
                }
            }
        }

    }
}