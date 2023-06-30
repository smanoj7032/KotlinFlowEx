package com.example.koltinflowex.presentation.views.activity.moviedetailactivity

import androidx.activity.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.databinding.MovieDetailBinding
import com.example.koltinflowex.presentation.common.base.BaseActivity
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import com.example.koltinflowex.presentation.views.fragment.moviedetail.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : BaseActivity<MovieDetailBinding>() {
    private val viewModel: MovieDetailViewModel by viewModels()
    var id: Int? = null
    override fun getLayoutResource(): Int {
        return R.layout.movie_detail
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun executeApiCall() {
        viewModel.getMovieDetail(id)
    }

    override fun executePagingApiCall() {

    }

    override fun onCreateView() {
        id = intent.getIntExtra("movieId", 0)
        setObserver()
    }

    private fun setObserver() {
        viewModel.movieDetail.customCollector(
            this,
            onLoading = ::onLoading,
            onError = ::onError,
            onSuccess = {
                setData(it)
            })
    }

    private fun setData(movieDetailsResponse: MovieDetail) {
        this.loadImage(
            POSTER_BASE_URL + movieDetailsResponse.poster_path,
            binding.imgMovie,
            binding.imgProgress
        )
        binding.apply {
            tvAppBarTitle.text = movieDetailsResponse.title.toString()
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