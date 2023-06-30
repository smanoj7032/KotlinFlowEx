package com.example.koltinflowex.presentation.views.fragment.movielist

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.emitter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {

    val searchCommentStateFlow = MutableStateFlow(State(Status.LOADING, CommentModel(), "", false))
    val allCommentsFlow = MutableStateFlow(State(Status.LOADING, listOf<CommentModel>(), "", false))
    val allPhotos = MutableStateFlow(State(Status.LOADING, listOf<PhotosResponse>(), "", false))
    val allMeme = MutableStateFlow(State(Status.LOADING, listOf<MemeResponse>(), "", false))
    val moviesListResponse =
        MutableStateFlow(State(Status.LOADING, MoviesListResponse(), "", false))
    val popularMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val upComingMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())
    val topRatedMovies = MutableStateFlow<State<PagingData<Result>>>(State.loading())


    fun getNewComment(id: Int?) {
        viewModelScope.launch { baseRepo.getComment(id).emitter(searchCommentStateFlow, false) }
    }

    fun getAllComment() {
        viewModelScope.launch { baseRepo.getAllComment().emitter(allCommentsFlow, false) }
    }

    private fun getAllPhotos() {
        viewModelScope.launch { baseRepo.getAllPhotos().emitter(allPhotos, false) }
    }

    private fun getMeme() {
        viewModelScope.launch { baseRepo.getMeme().emitter(allMeme, false) }
    }

    fun getMoviesList() {
        viewModelScope.launch {
            baseRepo.getMoviesList(viewModelScope).emitter(popularMovies, false)
        }
    } fun getTopRatedMoviesList() {
        viewModelScope.launch {
            baseRepo.getTopRatedMoviesList(viewModelScope).emitter(topRatedMovies, false)
        }
    } fun getUpcomingMoviesList() {
        viewModelScope.launch {
            baseRepo.getUpcomingMoviesList(viewModelScope).emitter(upComingMovies, false)
        }
    }
}