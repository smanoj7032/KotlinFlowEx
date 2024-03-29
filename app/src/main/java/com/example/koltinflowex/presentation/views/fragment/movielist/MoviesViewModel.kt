package com.example.koltinflowex.presentation.views.fragment.movielist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.koltinflowex.common.network.api.POPULAR_MOVIES
import com.example.koltinflowex.common.network.api.TOP_RATED_MOVIES
import com.example.koltinflowex.common.network.api.UPCOMING_MOVIES
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.MovieDetail
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
    val videoData = MutableLiveData<ArrayList<String>>()
    val popularMovies =  MutableStateFlow(State(Status.LOADING, MoviesListResponse(), null, false))
    val upComingMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val topRatedMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())

    fun getPopularMovies(page:Int) {
        viewModelScope.launch {
            baseRepo.getPopularMovies(page).emitter(popularMovies)
        }
    }

    fun getTopRatedMoviesList() {
        viewModelScope.launch {
            baseRepo.getTopRatedMoviesList().cachedIn(viewModelScope).collect {
                topRatedMovies.value = State.success(it)
            }
        }

    }

    fun getUpcomingMoviesList() {
        viewModelScope.launch {
            baseRepo.getUpcomingMoviesList().cachedIn(viewModelScope).collect {
                upComingMovies.value = State.success(it)
            }
        }

    }

    fun getSearchMovie(search: String, type: String) {
        viewModelScope.launch {}
        when (type) {
            POPULAR_MOVIES -> {
                viewModelScope.launch {
                    baseRepo.getSearchMovie(viewModelScope, search).cachedIn(viewModelScope)
                        .collect {
                           // popularMovies.value = State.success(it)
                        }
                }
            }

            TOP_RATED_MOVIES -> {
                viewModelScope.launch {
                    baseRepo.getSearchMovie(viewModelScope, search).cachedIn(viewModelScope)
                        .collect {
                            topRatedMovies.value = State.success(it)
                        }
                }
            }

            UPCOMING_MOVIES -> {
                viewModelScope.launch {
                    baseRepo.getSearchMovie(viewModelScope, search).cachedIn(viewModelScope)
                        .collect {
                            upComingMovies.value = State.success(it)
                        }
                }
            }
        }
    }

}