package com.example.koltinflowex.presentation.views.fragment.movielist

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.emitter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {

    val moviesListResponse =
        MutableStateFlow(State(Status.LOADING, MoviesListResponse(), "", false))
    val popularMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val upComingMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val topRatedMovies = MutableStateFlow<State<MoviesListResponse>>(State.loading())


    fun getPopularMovies() {
        viewModelScope.launch {
            baseRepo.getPopularMovies(viewModelScope).emitter(popularMovies, false)
        }
    }

    fun getTopRatedMoviesList() {
        viewModelScope.launch {
            baseRepo.getTopRatedMoviesList(viewModelScope).emitter(topRatedMovies, false)
        }
    }

    fun getUpcomingMoviesList() {
        viewModelScope.launch {
            baseRepo.getUpcomingMoviesList(viewModelScope).emitter(upComingMovies, false)
        }
    }

    fun getSearchMovie(search: String, type: String) {
        viewModelScope.launch {
            when (type) {
                "popularMovies" -> {
                    baseRepo.getSearchMovie(viewModelScope, search).emitter(popularMovies, false)
                }

               /* "topRated" -> {
                    baseRepo.getSearchMovie(viewModelScope, search).emitter(topRatedMovies, false)
                }*/

                "upComing" -> {
                    baseRepo.getSearchMovie(viewModelScope, search).emitter(upComingMovies, false)
                }
            }

        }
    }
}