package com.example.koltinflowex.presentation.views.fragment.moviedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.databinding.MovieDetailFragmentBinding
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<MovieDetailFragmentBinding>() {
    private val viewModel: MovieDetailViewModel by viewModels()
    val bundle = arguments
    var movieId=0


    override fun onCreateView(view: View, saveInstanceState: Bundle?) {setObserver() }

    override fun executePagingApiCall() {}

    override fun executeApiCall() {
        movieId= arguments?.getInt("movieId")!!
        viewModel.getMovieDetail(movieId)
    }

    override fun getLayoutResource(): Int {
        return R.layout.movie_detail_fragment
    }

    private fun setObserver() {
        viewModel.movieDetail.customCollector(
            this,
            onLoading = ::onLoading,
            onError = ::onError,
            onSuccess = { setData(it) })
    }

    private fun setData(movieDetailsResponse: MovieDetail) {
        activity?.loadImage(
            POSTER_BASE_URL + movieDetailsResponse.poster_path,
            binding.imgMovie
        )
        binding.apply {
            tvMovieTitle.text = movieDetailsResponse.title.toString()
            tvMovieDateRelease.text = movieDetailsResponse.release_date
            tvMovieOverview.text = movieDetailsResponse.overview
            tvMovieTagLine.text = movieDetailsResponse.tagline
            tvMovieRating.text = movieDetailsResponse.vote_average.toString()
            tvMovieRuntime.text = movieDetailsResponse.runtime.toString()
            tvMovieBudget.text = movieDetailsResponse.budget.toString()
            tvMovieRevenue.text = movieDetailsResponse.revenue.toString()
        }
    }

}