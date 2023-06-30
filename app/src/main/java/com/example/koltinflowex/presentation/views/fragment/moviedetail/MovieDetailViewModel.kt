package com.example.koltinflowex.presentation.views.fragment.moviedetail

import androidx.lifecycle.viewModelScope
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.emitter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {
    val movieDetail = MutableStateFlow(State(Status.LOADING, MovieDetail(), null, false))

    fun getMovieDetail(id: Int?) {
        viewModelScope.launch { baseRepo.getPopularMoviesDetails(id).emitter(movieDetail, false) }
    }
}