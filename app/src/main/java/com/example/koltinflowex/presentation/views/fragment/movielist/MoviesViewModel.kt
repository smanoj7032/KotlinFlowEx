package com.example.koltinflowex.presentation.views.fragment.movielist

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {

    val popularMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val upComingMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val topRatedMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())


    fun getPopularMovies() {
        viewModelScope.launch {
            popularMovies.value = State.loading()
            baseRepo.getPopularMovies().cachedIn(viewModelScope).collect {
                popularMovies.value = State.success(it)
            }
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
            "popular" -> {
                viewModelScope.launch {
                    baseRepo.getSearchMovie(viewModelScope, search).cachedIn(viewModelScope)
                        .collect {
                            popularMovies.value = State.success(it)
                        }
                }
            }

            "top_rated" -> {
                viewModelScope.launch {
                    baseRepo.getSearchMovie(viewModelScope, search).cachedIn(viewModelScope)
                        .collect {
                            topRatedMovies.value = State.success(it)
                        }
                }
            }

            "upcoming" -> {
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